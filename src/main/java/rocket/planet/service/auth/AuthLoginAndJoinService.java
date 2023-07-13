package rocket.planet.service.auth;

import static rocket.planet.domain.Profile.*;
import static rocket.planet.dto.auth.AuthDto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import rocket.planet.domain.Company;
import rocket.planet.domain.Department;
import rocket.planet.domain.Org;
import rocket.planet.domain.Profile;
import rocket.planet.domain.Role;
import rocket.planet.domain.Team;
import rocket.planet.domain.User;
import rocket.planet.domain.redis.EmailJoinConfirm;
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
import rocket.planet.repository.redis.EmailJoinConfirmRepository;
import rocket.planet.repository.redis.LastLoginRepository;
import rocket.planet.repository.redis.LimitLoginRepository;
import rocket.planet.repository.redis.RefreshTokenRedisRepository;
import rocket.planet.util.exception.AlreadyExistsIdException;
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

	private final UserRepository userRepository;

	private final ProfileRepository profileRepository;

	private final LastLoginRepository lastLoginRepository;

	private final EmailJoinConfirmRepository emailJoinConfirmRepository;

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

	/**
	 * 로그인 시작점
	 *
	 * @param dto
	 * @return CompleteLogin() => LoginResDto
	 * @패스워드 5회 틀릴 시 30분간 잠금
	 */
	@Transactional
	public LoginResDto checkLogin(LoginReqDto dto) throws Exception {

		User user = userRepository.findByUserId(dto.getId())
			.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다"));

		checkPasswordTryFiveValidation(dto, user);

		return completeLogin(user);
	}

	/**
	 * @param dto
	 * @param user
	 * @패스워드 5회 틀릴 시 30분간 잠금
	 */
	public void checkPasswordTryFiveValidation(LoginReqDto dto, User user) throws RedisException {
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

	/**
	 * @param user 5회 틀렸을시 저장하는 Redis Data 제거
	 *             권한 변경시 저장하는 Flag 제거
	 * @return LoginResDto
	 * @로그인 성공시 스프링 시큐리티 컨텍스트에 Authentication 객체 생성 및 주입
	 * @유저 프로필이 있을때와 없을때 분기 처리
	 * @마지막 로그인 시간 Redis에 저장
	 */

	private LoginResDto completeLogin(User user) throws Exception {
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

	/**
	 * @param user
	 * @기록이 없을때는 기본값 생성
	 * @마지막 로그인 시간 Redis에 저장
	 */

	private void saveLastLoginLogDataInRedis(User user) throws RedisException {
		Optional<LastLogin> lastLogin = lastLoginRepository.findById(user.getUserId());
		if (lastLogin.isEmpty()) {
			lastLogin = Optional.of(LastLogin.builder().email(user.getUserId()).build());
		}
		lastLogin.get().getLoginLogs().add(String.valueOf(LocalDateTime.now()));
		lastLoginRepository.save(lastLogin.get());
	}

	/**
	 * @param user 프로필 입력을 하지 않은 유저의 responseDto 생성
	 *             프로필 정보가 없으므로 권한을 뺀 responseDto 생성 및 Redis에 정보 저장
	 * @return
	 */

	private LoginResDto getLoginResDtoByNotCompleteJoinUser(User user) throws
		Exception {
		LoginResDto responseDto;
		responseDto = makeJsonWebTokenAndRoleDto(user);
		saveAuthInRedisForJoinUser(user, responseDto);
		return responseDto;
	}

	/**
	 * @param user 프로필 정보가 있기 때문에 Db에서 권한 정보를 가져와서 Redis에 함께 저장
	 * @return
	 */

	private LoginResDto getLoginResDtoByCompleteJoinUser(User user) throws
		Exception {
		LoginResDto responseDto;
		responseDto = makeJsonWebTokenAndRoleDto(user);
		saveAuthInRedisForJoinUser(user, responseDto, getUserAuthoritiesByDb(user));
		return responseDto;
	}

	/**
	 * @param user
	 * @return
	 * @DB에서 권한 정보 가져오기
	 */

	private List<RedisCacheAuth> getUserAuthoritiesByDb(User user) throws Exception {
		return authRepository.findAllByProfileAuthority_ProfileUserId(
				user.getUserId()).stream().map(e -> RedisCacheAuth.builder().authorityTargetTable(e.getAuthType())
				.authorityTargetUid(e.getAuthTargetId()).build())
			.collect(
				Collectors.toList());
	}

	/**
	 * @param user
	 * @return
	 * @Jwt 토큰 생성 메소드(로그인이 성공했거나, 회원 가입시 자동 로그인)
	 * @유저 프로필이 있을때와 없을 때 분기 처리
	 */
	@Transactional
	private LoginResDto makeJsonWebTokenAndRoleDto(User user) throws Exception {

		String userName = user.getUserId();

		String authority;

		if (Objects.isNull(user.getProfile())) {
			authority = "ROLE_" + Role.CREW.name();
		} else {
			authority = "ROLE_" + user.getProfile().getRole().name();
		}
		return getResponseDtoByUserData(user, userName, authority);
	}

	/**
	 * @param user
	 * @param responseDto
	 * @프로필 정보가 없을때 RefreshToken Redis에 저장
	 */
	private void saveAuthInRedisForJoinUser(User user, LoginResDto responseDto) throws RedisException {
		refreshTokenRedisRepository.save(RefreshToken.builder().token(responseDto.getRefreshToken())
			.email(user.getUserId())
			.build());
	}

	/**
	 * @param user
	 * @param responseDto
	 * @프로필 정보가 있을때 RefreshToken Redis에 저장
	 */

	private void saveAuthInRedisForJoinUser(User user, LoginResDto responseDto,
		List<RedisCacheAuth> userAuthorities) throws RedisException {
		refreshTokenRedisRepository.save(RefreshToken.builder().token(responseDto.getRefreshToken())
			.email(user.getUserId())
			.authorities(userAuthorities)
			.build());
	}

	/**
	 * @param user
	 * @param userName
	 * @param authority
	 * @return
	 * @프로필 정보를 분기로 LoginResDto 생성
	 */

	private LoginResDto getResponseDtoByUserData(User user, String userName, String authority) throws
		Exception {
		LoginResDto loginResDto;

		Profile profile = user.getProfile();

		if (Objects.isNull(profile)) {
			loginResDto = makeLoginResBuilder(user, userName, authority);
		} else {
			loginResDto = makeLoginResBuilder(user, userName, authority, profile);
		}

		return loginResDto;
	}

	/**
	 * @param user
	 * @param userName
	 * @param authority
	 * @return
	 * @프로필 정보가 없을때 LoginResDto 생성
	 */

	private LoginResDto makeLoginResBuilder(User user, String userName, String authority) throws
		Exception {
		return LoginResDto.builder()
			.authRole(roleIssue(authority))
			.isThreeMonth(checkHasItBeenThreeMonthsSinceTheLastPasswordChange(user))
			.userNickName(idToUserNickName(userName))
			.grantType(GRANT_TYPE)
			.accessToken(jwtIssuer.createAccessToken(userName, authority))
			.refreshToken(jwtIssuer.createRefreshToken(userName, authority))
			.build();
	}

	/**
	 * @param user
	 * @param userName
	 * @param authority
	 * @param profile
	 * @return
	 * @프로필 정보가 있을때 LoginResDto 생성
	 */

	private LoginResDto makeLoginResBuilder(User user, String userName, String authority, Profile profile) throws
		Exception {

		AuthOrg authOrg = getProfileToAuthOrg(profile);
		return LoginResDto.builder()
			.authRole(roleIssue(authority))
			.authOrg(authOrg)
			.isThreeMonth(checkHasItBeenThreeMonthsSinceTheLastPasswordChange(user))
			.userNickName(idToUserNickName(userName))
			.grantType(GRANT_TYPE)
			.accessToken(jwtIssuer.createAccessToken(userName, authority))
			.refreshToken(jwtIssuer.createRefreshToken(userName, authority))
			.build();
	}

	/**
	 * @param profile profile로 부터 AuthOrg 객체 생성
	 * @return
	 */

	private AuthOrg getProfileToAuthOrg(Profile profile) throws Exception {
		List<Org> org = profile.getOrg();
		return AuthOrg.builder()
			.companyName(org.get(0).getCompany().getCompanyName())
			.deptName(org.get(0).getDepartment().getDeptName())
			.teamName(org.get(0).getTeam().getTeamName()).build();
	}

	/**
	 * @param user
	 * @return boolean
	 * @유저의 비밀번호 변경일이 3개월이 지났는지 확인
	 */

	private boolean checkHasItBeenThreeMonthsSinceTheLastPasswordChange(User user) throws
		Exception {
		return user.getLastPwdModifiedDt().isBefore(LocalDate.now().minusDays(90));
	}

	/**
	 * @param bearerToken
	 * @return
	 * @RefreshToken RefreshToken 해체
	 */

	private String makeResolveToken(String bearerToken) throws Exception {

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(GRANT_TYPE)) {
			return bearerToken.substring(7);
		}

		return null;
	}

	/**
	 * @param bearerToken
	 * @return
	 * @RefreshToken 해체 후 유효성 검사 및 새로운 토큰 발급
	 */

	@Transactional
	public LoginResDto makeReissue(String bearerToken) throws Exception {

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

	/**
	 * @param refreshToken
	 * @param claims       Claim을 바탕으로 새로운 Token과 LoginResDto 생성
	 * @return
	 */

	private LoginResDto getReissueResponseDto(String refreshToken, Claims claims) throws
		Exception {
		Optional<User> user = userRepository.findByUserId(claims.getSubject());
		String roles = roleIssue(claims.get("roles").toString());

		return LoginResDto.builder()
			.grantType(GRANT_TYPE)
			.authOrg(getProfileToAuthOrg(user.get().getProfile()))
			.authRole(roleIssue(roles))
			.userNickName(idToUserNickName(claims.getSubject()))
			.accessToken(jwtIssuer.createAccessToken(claims.getSubject(), claims.get("roles").toString()))
			.refreshToken(refreshToken)
			.build();
	}

	private String roleIssue(String role) {
		String issueRole = role;

		if (role.contains("[")) {
			issueRole = role.replace("[", "").replace("]", "");
		}

		if (role.contains("ROLE_")) {
			issueRole = role.replace("ROLE_", "");
		}

		return issueRole;
	}

	/**
	 * @param refreshToken
	 * @param redisRefreshToken
	 * @return
	 * @Redis에 저장된 RefreshToken과 요청된 RefreshToken 비교 (캐싱 활용)
	 */
	private boolean checkRefreshTokenInRedis(String refreshToken, RefreshToken redisRefreshToken) throws
		Exception {
		return redisRefreshToken.getToken().equals(refreshToken) ? true : false;
	}

	/**
	 * @param refreshToken
	 * @return
	 * @RefreshToken 유효성 체크
	 */
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

	/**
	 * @param dto
	 * @return
	 * @회원가입 단순 계정정보에 대한 회원가입
	 * 자동 로그인 처리
	 */

	@Transactional
	public LoginResDto checkJoin(JoinReqDto dto) throws Exception {
		EmailJoinConfirm emailConfirm = emailJoinConfirmRepository.findById(dto.getId())
			.orElseThrow(() -> new NoValidEmailTokenException());

		emailJoinConfirmRepository.delete(emailConfirm);

		userRepository.findByUserId(dto.getId())
			.ifPresent(user -> {
				throw new AlreadyExistsIdException();
			});
		User saveUser = userRepository.save(User.defaultUser(dto.getId(), dto.getPassword()));
		saveLastLoginDataInRedis(dto);
		return completeLogin(saveUser);
	}

	private void saveLastLoginDataInRedis(JoinReqDto dto) throws RedisException {
		lastLoginRepository.save(LastLogin.builder().email(dto.getId()).build());
	}

	/**
	 * @param dto
	 * @return
	 * @기본 정보 등록(update)
	 */

	@Transactional
	public BasicInputResDto saveBasicProfile(BasicInputReqDto dto) throws Exception {

		String id = Optional.of(UserDetailsImpl.getLoginUserId())
			.orElseThrow(() -> new UsernameNotFoundException("토큰이 만료되어 다시 로그인 해주세요"));

		Profile profile = BasicInsertDtoToProfile(dto, id);

		User user = userRepository.findByUserId(id)
			.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));

		profile = profileRepository.save(profile);
		user.updateProfile(profile);
		Org org = makeOrgByCompanyAndDeptAndTeam(dto, profile);

		return makeBasicInputResDto(profile, user, org, id);
	}

	/**
	 * @param dto
	 * @param profile
	 * @return
	 * @회사, 부서, 팀 정보를 바탕으로 Org 생성
	 */

	@Transactional
	public Org makeOrgByCompanyAndDeptAndTeam(BasicInputReqDto dto, Profile profile) throws
		Exception {
		Company dkTechIn = companyRepository.findByCompanyName("dktechin");
		Team team = teamRepository.findByTeamName(dto.getTeamName());
		Department department = deptRepository.findByDeptName(dto.getDeptName());
		Org org = Org.joinDefaultOrg(dkTechIn, profile, department, team, true);
		return orgRepository.save(org);
	}

	/**
	 * @param profile
	 * @param user
	 * @param org
	 * @param id
	 * @return
	 * @기본 정보 응답 DTO 생성
	 */

	private BasicInputResDto makeBasicInputResDto(Profile profile, User user, Org org, String id) throws
		Exception {
		return BasicInputResDto
			.builder().authOrg(AuthOrg.builder().teamName(org.getTeam().getTeamName())
				.deptName(org.getDepartment().getDeptName())
				.companyName(org.getCompany().getCompanyName()).build())
			.userNickName(idToUserNickName(id))
			.authRole(roleIssue(profile.getRole().name()))
			.isThreeMonth(checkHasItBeenThreeMonthsSinceTheLastPasswordChange(user))
			.build();
	}

}