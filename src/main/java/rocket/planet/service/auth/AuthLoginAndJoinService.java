package rocket.planet.service.auth;

import static rocket.planet.domain.Profile.*;
import static rocket.planet.dto.auth.AuthDto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
import rocket.planet.domain.Company;
import rocket.planet.domain.Department;
import rocket.planet.domain.Org;
import rocket.planet.domain.Profile;
import rocket.planet.domain.Role;
import rocket.planet.domain.Team;
import rocket.planet.domain.User;
import rocket.planet.domain.redis.LastLogin;
import rocket.planet.domain.redis.LimitLogin;
import rocket.planet.domain.redis.RedisCacheAuth;
import rocket.planet.domain.redis.RefreshToken;
import rocket.planet.dto.auth.AuthDto.LoginResDto.AuthOrg;
import rocket.planet.repository.jpa.AuthRepository;
import rocket.planet.repository.jpa.CompanyRepository;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.OrgRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.TeamRepository;
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
import rocket.planet.util.security.JsonWebTokenIssuer;
import rocket.planet.util.security.UserDetailsImpl;

@Service
@RequiredArgsConstructor
public class AuthLoginAndJoinService {

	private static final String GRANT_TYPE = "Bearer";

	private static final String COMPLETE_JOIN = "기본 정보 입력 완료";

	private final UserRepository userRepository;

	private final ProfileRepository profileRepository;

	private final LastLoginRepository lastLoginRepository;

	private final EmailConfirmRepository emailConfirmRepository;

	private final TeamRepository teamRepository;

	private final DeptRepository deptRepository;

	private final RefreshTokenRedisRepository refreshTokenRedisRepository;

	private final LimitLoginRepository limitLoginRepository;

	private final CompanyRepository companyRepository;

	private final AuthChangeRepository authChangeRepository;

	private final OrgRepository orgRepository;

	private final AuthRepository authRepository;

	private final PasswordEncoder passwordEncoder;

	private final JsonWebTokenIssuer jwtIssuer;

	@Transactional
	public LoginResDto checkLogin(LoginReqDto dto) {

		User user = userRepository.findByUserId(dto.getId())
			.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));

		checkPasswordTryFiveValidation(dto, user);

		return completeLogin(user);
	}

	private void checkPasswordTryFiveValidation(LoginReqDto dto, User user) {
		if (!passwordEncoder.matches(dto.getPassword(), user.getUserPwd())) {
			int count;
			if (limitLoginRepository.findById(user.getUserId()).isPresent()
				&& limitLoginRepository.findById(user.getUserId()).get().getCount() == 4) {
				throw new Temp30MinuteLockException();
			}
			if (limitLoginRepository.findById(user.getUserId()).isPresent()) {
				LimitLogin limitLogin = limitLoginRepository.findById(user.getUserId()).get();
				count = limitLogin.add();
				limitLoginRepository.save(limitLogin);
			} else {
				limitLoginRepository.save(LimitLogin.builder().count(1).email(user.getUserId()).build());
				count = 1;
			}
			throw new PasswordMismatchException("비밀번호가 일치하지 않습니다." + count + "회");
		}
	}

	private LoginResDto completeLogin(User user) {
		limitLoginRepository.deleteById(user.getUserId());
		authChangeRepository.deleteById(user.getUserId());

		LoginResDto responseDto;

		Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUserId(), user.getUserPwd());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		if (user.isExistProfile()) {
			responseDto = getLoginResDtoByCompleteJoinUser(user);
		} else {
			responseDto = getLoginResDtoByNotCompleteJoinUser(user);
		}

		saveLastLoginLogDataInRedis(user);

		return responseDto;
	}

	private void saveLastLoginLogDataInRedis(User user) {
		Optional<LastLogin> lastLogin = lastLoginRepository.findById(user.getUserId());
		if (lastLogin.isEmpty()) {
			lastLogin = Optional.of(LastLogin.builder().email(user.getUserId()).build());
		}
		lastLoginRepository.save(lastLogin.get());
	}

	private LoginResDto getLoginResDtoByNotCompleteJoinUser(User user) {
		LoginResDto responseDto;
		responseDto = makeJsonWebTokenAndRoleDto(user);
		saveAuthInRedisForNotCompleteJoinUser(user, responseDto);
		return responseDto;
	}

	private LoginResDto getLoginResDtoByCompleteJoinUser(User user) {
		LoginResDto responseDto;
		responseDto = makeJsonWebTokenAndRoleDto(user);
		saveAuthInRedisForCompleteJoinUser(user, responseDto, getUserAuthoritiesByDb(user));
		return responseDto;
	}

	private LoginResDto makeJsonWebTokenAndRoleDto(User user) {

		String userName = user.getUserId();

		String authority;

		if (Objects.isNull(user.getProfile())) {
			authority = Role.CREW.name();
		} else {
			authority = user.getProfile().getRole().name();
		}
		return getResponseDtoByOrg(user, userName, authority);
	}

	private List<RedisCacheAuth> getUserAuthoritiesByDb(User user) {
		return authRepository.findAllByProfileAuthority_ProfileUserId(
				user.getUserId()).stream().map(e -> RedisCacheAuth.builder().authorityTargetTable(e.getAuthType())
				.authorityTargetUid(e.getAuthTargetId()).build())
			.collect(
				Collectors.toList());
	}

	private void saveAuthInRedisForNotCompleteJoinUser(User user, LoginResDto responseDto) {
		refreshTokenRedisRepository.save(RefreshToken.builder().token(responseDto.getRefreshToken())
			.email(user.getUserId())
			.build());
	}

	private void saveAuthInRedisForCompleteJoinUser(User user, LoginResDto responseDto,
		List<RedisCacheAuth> userAuthorities) {
		refreshTokenRedisRepository.save(RefreshToken.builder().token(responseDto.getRefreshToken())
			.email(user.getUserId())
			.authorities(userAuthorities)
			.build());
	}

	private LoginResDto getResponseDtoByOrg(User user, String userName, String authority) {
		LoginResDto loginResDto;

		Profile profile = user.getProfile();

		if (Objects.isNull(profile)) {
			loginResDto = makeLoginResBuilderNoProfile(user, userName, authority);
		} else {
			loginResDto = makeLoginResBuilderProfile(user, userName, authority, profile);
		}

		return loginResDto;
	}

	private LoginResDto makeLoginResBuilderNoProfile(User user, String userName, String authority) {
		return LoginResDto.builder()
			.authRole(authority)
			.isThreeMonth(checkHasItBeenThreeMonthsSinceTheLastPasswordChange(user))
			.userNickName(idToUserNickName(userName))
			.grantType(GRANT_TYPE)
			.accessToken(jwtIssuer.createAccessToken(userName, authority))
			.refreshToken(jwtIssuer.createRefreshToken(userName, authority))
			.build();
	}

	private LoginResDto makeLoginResBuilderProfile(User user, String userName, String authority, Profile profile) {

		AuthOrg authOrg = getProfileToAuthOrg(profile);
		return LoginResDto.builder()
			.authRole(authority)
			.authOrg(authOrg)
			.isThreeMonth(checkHasItBeenThreeMonthsSinceTheLastPasswordChange(user))
			.userNickName(idToUserNickName(userName))
			.grantType(GRANT_TYPE)
			.accessToken(jwtIssuer.createAccessToken(userName, authority))
			.refreshToken(jwtIssuer.createRefreshToken(userName, authority))
			.build();
	}

	private AuthOrg getProfileToAuthOrg(Profile profile) {
		List<Org> org = profile.getOrg();
		return AuthOrg.builder()
			.companyName(org.get(0).getCompany().getCompanyName())
			.deptName(org.get(0).getDepartment().getDeptName())
			.teamName(org.get(0).getTeam().getTeamName()).build();
	}

	private boolean checkHasItBeenThreeMonthsSinceTheLastPasswordChange(User user) {
		return user.getLastPwdModifiedDt().isBefore(LocalDate.now().minusDays(90));
	}

	private String makeResolveToken(String bearerToken) {

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(GRANT_TYPE)) {
			return bearerToken.substring(7);
		}

		return null;
	}

	public LoginResDto makeReissue(String bearerToken) {

		String refreshToken = makeResolveToken(bearerToken);
		Claims claims = getClaimsWithValidCheck(refreshToken);

		RefreshToken redisRefreshToken = refreshTokenRedisRepository.findById(claims.getSubject())
			.orElseThrow(() -> new UsernameNotFoundException("다시 로그인 해주세요"));

		if (checkRefreshTokenInRedis(refreshToken, redisRefreshToken)) {
			return getReissueResponseDto(refreshToken, claims);
		}

		User findUserByJwtSubject = userRepository.findByUserId(claims.getSubject())
			.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));

		return makeJsonWebTokenAndRoleDto(findUserByJwtSubject);
	}

	private LoginResDto getReissueResponseDto(String refreshToken, Claims claims) {
		return LoginResDto.builder()
			.grantType(GRANT_TYPE)
			.accessToken(jwtIssuer.createAccessToken(claims.getSubject(), claims.get("roles", String.class)))
			.refreshToken(refreshToken)
			.build();
	}

	private boolean checkRefreshTokenInRedis(String refreshToken, RefreshToken redisRefreshToken) {
		return redisRefreshToken.getToken().equals(refreshToken) ? true : false;
	}

	private Claims getClaimsWithValidCheck(String refreshToken) {
		if (!StringUtils.hasText(refreshToken)) {
			throw new JwtInvalidException("not exists refresh token");
		}

		Claims claims = jwtIssuer.parseClaimsFromRefreshToken(refreshToken);
		if (claims == null) {
			throw new JwtInvalidException("not exists claims in token");
		}
		return claims;
	}

	@Transactional
	public LoginResDto checkJoin(JoinReqDto dto) {
		emailConfirmRepository.findById(dto.getId())
			.orElseThrow(() -> new NoValidEmailTokenException());
		User saveUser = userRepository.save(User.defaultUser(dto.getId(), dto.getPassword()));
		lastLoginRepository.save(LastLogin.builder().email(dto.getId()).build());
		emailConfirmRepository.deleteById(dto.getId());
		return completeLogin(saveUser);
	}

	@Transactional
	public BasicInputResDto saveBasicProfileAndAutoLogin(BasicInputReqDto dto) {
		String id = UserDetailsImpl.getLoginUserId();
		Profile profile = BasicInsertDtoToProfile(dto, id);

		User user = userRepository.findByUserId(id)
			.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));
		profile = profileRepository.save(profile);
		user.updateProfile(profile);
		Org org = makeOrgByCompanyAndDeptAndTeam(dto, profile);

		return makeBasicInputResDto(profile, user, org, id);
	}

	private BasicInputResDto makeBasicInputResDto(Profile profile, User user, Org org, String id) {
		return BasicInputResDto
			.builder().authOrg(AuthOrg.builder().teamName(org.getTeam().getTeamName())
				.deptName(org.getDepartment().getDeptName())
				.companyName(org.getCompany().getCompanyName()).build())
			.userNickName(idToUserNickName(id))
			.authRole(profile.getRole().name())
			.isThreeMonth(checkHasItBeenThreeMonthsSinceTheLastPasswordChange(user))
			.build();
	}

	@Transactional
	public Org makeOrgByCompanyAndDeptAndTeam(BasicInputReqDto dto, Profile profile) {
		Company dkTechIn = companyRepository.findByCompanyName("dktechin");
		Team team = teamRepository.findByTeamName(dto.getTeamName());
		Department department = deptRepository.findByDeptName(dto.getDeptName());
		Org org = Org.joinDefaultOrg(dkTechIn, profile, department, team, true);
		return orgRepository.save(org);
	}
}