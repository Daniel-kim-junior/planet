package rocket.planet.service.auth;

import static rocket.planet.dto.auth.AuthDto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import rocket.planet.domain.Profile;
import rocket.planet.domain.ProfileAuthority;
import rocket.planet.domain.Role;
import rocket.planet.domain.User;
import rocket.planet.domain.redis.EmailConfirm;
import rocket.planet.domain.redis.LastLogin;
import rocket.planet.domain.redis.LimitLogin;
import rocket.planet.domain.redis.RefreshToken;
import rocket.planet.repository.jpa.AuthRepository;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.repository.redis.AuthChangeRepository;
import rocket.planet.repository.redis.EmailConfirmRepository;
import rocket.planet.repository.redis.LastLoginRepository;
import rocket.planet.repository.redis.LimitLoginRepository;
import rocket.planet.repository.redis.RefreshTokenRedisRepository;
import rocket.planet.util.exception.JwtInvalidException;
import rocket.planet.util.exception.NoValidEmailTokenException;
import rocket.planet.util.exception.PasswordMismatchException;
import rocket.planet.util.exception.Temp30MinuteLockException;

@Service
@RequiredArgsConstructor
public class AuthLoginAndJoinService {
	private static final String GRANT_TYPE = "Bearer";

	private static final String COMPLETE_JOIN = "회원 가입이 완료 되었습니다.";

	private final UserRepository userRepository;

	private final LastLoginRepository lastLoginRepository;

	private final EmailConfirmRepository emailConfirmRepository;

	private final RefreshTokenRedisRepository refreshTokenRedisRepository;

	private final LimitLoginRepository limitLoginRepository;

	private final AuthChangeRepository authChangeRepository;

	private final AuthRepository authRepository;


	private final PasswordEncoder passwordEncoder;

	private final JsonWebTokenIssuer jwtIssuer;

	public LoginResponseDto authLogin(LoginReqDto dto) {

		User user = userRepository.findByUserId(dto.getId())
			.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));
		if (!passwordEncoder.matches(dto.getPassword(), user.getUserPwd())) {
			int count;
			if(limitLoginRepository.findById(user.getUserId()).isPresent() && limitLoginRepository.findById(user.getUserId()).get().getCount() == 4) {
				throw new Temp30MinuteLockException();
			}

			if(limitLoginRepository.findById(user.getUserId()).isPresent()) {
				LimitLogin limitLogin = limitLoginRepository.findById(user.getUserId()).get();
				count = limitLogin.add();
				limitLoginRepository.save(limitLogin);
			} else {
				limitLoginRepository.save(LimitLogin.builder().count(1).email(user.getUserId()).build());
				count = 1;
			}
			throw new PasswordMismatchException("비밀번호가 일치하지 않습니다." + count + "회");
		}
		System.out.println(user);

		return completeLogin(user);
	}

	private LoginResponseDto completeLogin(User user) {
		limitLoginRepository.deleteById(user.getUserId());
		authChangeRepository.deleteById(user.getUserId());
		LoginResponseDto responseDto;

		Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUserId(), user.getUserPwd());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		if(user.isExistProfile()) {
			// 첫 로그인
			// Profile profile = user.getProfile();
			// List<ProfileAuthority> authority = profile.getAuthority();
			// responseDto = createJsonWebTokenAndRoleDto(user);
			// refreshTokenRedisRepository.save(RefreshToken.builder().token(responseDto.getRefreshToken())
			// 		.email(user.getUserId()).authorities(authority).build());
		} else {
			responseDto = createJsonWebTokenAndRoleDto(user);
			Optional<LastLogin> lastLogin = lastLoginRepository.findById(user.getUserId());
			lastLogin.get().add(String.valueOf(LocalDateTime.now()));
			lastLoginRepository.save(lastLogin.get());
			refreshTokenRedisRepository.save(RefreshToken.builder().token(responseDto.getRefreshToken())
				.email(user.getUserId()).build());
			return responseDto;

		}
		return null;

	}

	public static String makeUserNickName(String email) {
		return email.split("@")[0];
	}

	private LoginResponseDto createJsonWebTokenAndRoleDto(User user) {

		String userName = user.getUserId();
		String authority;
		if(user.getProfile() == null) {
			authority = Role.CREW.name();
		} else {
			authority = user.getProfile().getRole().name();
		}


		return LoginResponseDto.builder()
			.authRole(authority)
			.grantType(GRANT_TYPE)
			.accessToken(jwtIssuer.createAccessToken(userName, authority))
			.refreshToken(jwtIssuer.createRefreshToken(userName, authority))
			.build();
	}

	private String resolveToken(String bearerToken) {

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(GRANT_TYPE)) {
			return bearerToken.substring(7);
		}

		return null;
	}

	public LoginResponseDto reissue(String bearerToken) {

		String refreshToken = resolveToken(bearerToken);

		if (!StringUtils.hasText(refreshToken)) {
			throw new JwtInvalidException("not exists refresh token");
		}

		Claims claims = jwtIssuer.parseClaimsFromRefreshToken(refreshToken);
		if (claims == null) {
			throw new JwtInvalidException("not exists claims in token");
		}

		User user = userRepository.findByUserId(claims.getSubject())
			.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));

		return createJsonWebTokenAndRoleDto(user);
	}

	@Transactional
	public String authJoin(JoinReqDto dto) {
		emailConfirmRepository.findById(dto.getId())
			.orElseThrow(() -> new NoValidEmailTokenException());
		userRepository.save(User.defaultUser(dto.getId(), dto.getPassword()));
		lastLoginRepository.save(LastLogin.builder().email(dto.getId()).build());
		emailConfirmRepository.deleteById(dto.getId());
		return COMPLETE_JOIN;
	}

	// public LoginResponseDto joinAndLogin(BasicInputReqDto dto) {
	//
	// 	return createJsonWebTokenAndRoleDto(user);
	// }

}
