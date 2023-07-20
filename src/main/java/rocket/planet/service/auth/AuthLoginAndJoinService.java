package rocket.planet.service.auth;

import static rocket.planet.domain.Profile.*;
import static rocket.planet.dto.auth.AuthDto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.lettuce.core.RedisException;
import rocket.planet.domain.Authority;
import rocket.planet.domain.Company;
import rocket.planet.domain.Department;
import rocket.planet.domain.Org;
import rocket.planet.domain.Profile;
import rocket.planet.domain.Role;
import rocket.planet.domain.Team;
import rocket.planet.domain.User;
import rocket.planet.domain.redis.AccessToken;
import rocket.planet.domain.redis.EmailJoinConfirm;
import rocket.planet.domain.redis.LastLogin;
import rocket.planet.domain.redis.LimitLogin;
import rocket.planet.domain.redis.RefreshToken;
import rocket.planet.dto.auth.AuthDto.LoginResDto.AuthOrg;
import rocket.planet.repository.jpa.AuthRepository;
import rocket.planet.repository.jpa.CompanyRepository;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.OrgRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.TeamRepository;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.repository.redis.AccessTokenRedisRepository;
import rocket.planet.repository.redis.EmailJoinConfirmRepository;
import rocket.planet.repository.redis.LastLoginRepository;
import rocket.planet.repository.redis.LimitLoginRepository;
import rocket.planet.repository.redis.RefreshTokenRedisRepository;
import rocket.planet.util.exception.AlreadyExistsIdException;
import rocket.planet.util.exception.IdMismatchException;
import rocket.planet.util.exception.JwtInvalidException;
import rocket.planet.util.exception.NoSuchEmailException;
import rocket.planet.util.exception.NoValidEmailTokenException;
import rocket.planet.util.exception.PasswordMismatchException;
import rocket.planet.util.exception.Temp30MinuteLockException;
import rocket.planet.util.security.JsonWebTokenIssuer;

@Service
public class AuthLoginAndJoinService {

	private static final String GRANT_TYPE = "Bearer";

	private UserRepository userRepository;

	private ProfileRepository profileRepository;

	private LastLoginRepository lastLoginRepository;

	private EmailJoinConfirmRepository emailJoinConfirmRepository;

	private TeamRepository teamRepository;

	private DeptRepository deptRepository;

	private RefreshTokenRedisRepository refreshTokenRedisRepository;

	private AccessTokenRedisRepository accessTokenRedisRepository;

	private LimitLoginRepository limitLoginRepository;

	private CompanyRepository companyRepository;

	private OrgRepository orgRepository;

	private AuthRepository authRepository;

	private PasswordEncoder passwordEncoder;

	private JsonWebTokenIssuer jwtIssuer;

	public AuthLoginAndJoinService(UserRepository userRepository, ProfileRepository profileRepository,
		LastLoginRepository lastLoginRepository, EmailJoinConfirmRepository emailJoinConfirmRepository,
		TeamRepository teamRepository, DeptRepository deptRepository,
		RefreshTokenRedisRepository refreshTokenRedisRepository, AccessTokenRedisRepository accessTokenRedisRepository,
		LimitLoginRepository limitLoginRepository, CompanyRepository companyRepository, OrgRepository orgRepository,
		AuthRepository authRepository, PasswordEncoder passwordEncoder, JsonWebTokenIssuer jwtIssuer) {
		this.userRepository = userRepository;
		this.profileRepository = profileRepository;
		this.lastLoginRepository = lastLoginRepository;
		this.emailJoinConfirmRepository = emailJoinConfirmRepository;
		this.teamRepository = teamRepository;
		this.deptRepository = deptRepository;
		this.refreshTokenRedisRepository = refreshTokenRedisRepository;
		this.accessTokenRedisRepository = accessTokenRedisRepository;
		this.limitLoginRepository = limitLoginRepository;
		this.companyRepository = companyRepository;
		this.orgRepository = orgRepository;
		this.authRepository = authRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtIssuer = jwtIssuer;
	}

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
			.orElseThrow(() -> new IdMismatchException("존재하지 않는 id 입니다"));

		checkPasswordTryFiveValidation(dto, user);

		return completeLogin(user);
	}

	/**
	 * @param dto
	 * @param user
	 * @패스워드 5회 틀릴 시 30분간 잠금
	 */
	public void checkPasswordTryFiveValidation(LoginReqDto dto, User user) throws RedisException, NullPointerException {
		if (!passwordEncoder.matches(dto.getPassword(), user.getUserPwd())) {
			limitLoginRepository.findById(user.getUserId()).ifPresent(limitLogin -> {
				if (limitLogin.getCount() == 4) {
					throw new Temp30MinuteLockException();
				}
			});

			int count;

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

	public LoginResDto completeLogin(User user) throws Exception {
		limitLoginRepository.deleteById(user.getUserId());

		saveLastLoginLogDataInRedis(user);

		String userName = user.getUserId();

		Profile profile = user.getProfile();

		LoginResDto loginResDto;

		String authority;

		if (Objects.isNull(profile)) {
			authority = "ROLE_" + Role.CREW.name();
			loginResDto = makeLoginResBuilder(user, userName, authority);
		} else {
			authority = "ROLE_" + profile.getRole().name();
			loginResDto = makeLoginResBuilder(user, userName, authority, profile);
		}

		saveAuthInRedisForJoinUser(user, loginResDto);

		return loginResDto;
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
	 * @param user
	 * @return
	 * @DB에서 권한 정보 가져오기
	 */

	private List<Authority> getUserAuthoritiesByDb(User user) throws Exception {
		return authRepository.findAllByProfileAuthority_ProfileUserId(user.getUserId());
	}

	/**
	 * @param user
	 * @param responseDto
	 * @프로필 정보가 없을때 RefreshToken, AccessToken Redis에 저장
	 */
	private void saveAuthInRedisForJoinUser(User user, LoginResDto responseDto) throws RedisException {
		refreshTokenRedisRepository.save(RefreshToken.builder().token(responseDto.getRefreshToken())
			.email(user.getUserId())
			.build());
		accessTokenRedisRepository.save(AccessToken.builder().token(responseDto.getAccessToken())
			.email(user.getUserId())
			.build());
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
			.authRole(roleToClientRes(authority))
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
			.authRole(roleToClientRes(authority))
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

	public AuthOrg getProfileToAuthOrg(Profile profile) throws Exception {
		List<Org> org = profile.getOrg();

		Optional<Company> company = Optional.ofNullable(org.get(0).getCompany());
		Optional<Department> department = Optional.ofNullable(org.get(0).getDepartment());
		Optional<Team> team = Optional.ofNullable(org.get(0).getTeam());

		return AuthOrg.builder()
			.companyName(company.isEmpty() ? null : company.get().getCompanyName())
			.deptName(department.isEmpty() ? null : department.get().getDeptName())
			.teamName(team.isEmpty() ? null : team.get().getTeamName()).build();
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
			.orElseThrow(() -> new JwtInvalidException("Refresh Token이 만료되었습니다."));

		if (checkRefreshTokenInRedis(refreshToken, redisRefreshToken)) {
			return getReissueResponseDto(refreshToken, claims);
		}

		User findUserByJwtSubject = userRepository.findByUserId(claims.getSubject())
			.orElseThrow(() -> new IdMismatchException("존재 하지 않는 id 입니다"));

		return completeLogin(findUserByJwtSubject);
	}

	/**
	 * @param refreshToken
	 * @param claims  Claim을 바탕으로 새로운 Token과 LoginResDto 생성
	 * @return
	 */

	private LoginResDto getReissueResponseDto(String refreshToken, Claims claims) throws
		Exception {

		User user = userRepository.findByUserId(claims.getSubject())
			.orElseThrow(NoSuchEmailException::new);

		String singleTonRole = claims.get("roles").toString();
		String role = singleTonRole.substring(1, singleTonRole.length() - 1);
		String accessToken = jwtIssuer.createAccessToken(claims.getSubject(), role);

		accessTokenRedisRepository.save(AccessToken.builder().token(accessToken)
			.email(user.getUserId())
			.build());
		return LoginResDto.builder()
			.grantType(GRANT_TYPE)
			.authOrg(getProfileToAuthOrg(user.getProfile()))
			.authRole(roleToClientRes(role))
			.userNickName(idToUserNickName(claims.getSubject()))
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	private String roleToClientRes(String role) {
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
	public boolean checkRefreshTokenInRedis(String refreshToken, RefreshToken redisRefreshToken) throws
		Exception {
		return redisRefreshToken.getToken().equals(refreshToken) ? true : false;
	}

	/**
	 * @param refreshToken
	 * @return
	 * @RefreshToken 유효성 체크
	 */
	public Claims getClaimsWithValidCheck(String refreshToken) {
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
	public BasicInputResDto saveBasicProfile(BasicInputReqDto dto, User user) throws Exception {
		Optional.ofNullable(user)
			.orElseThrow(() -> new UsernameNotFoundException("토큰이 만료되어 다시 로그인 해주세요"));

		Profile basicProfile = BasicInsertDtoToProfile(dto, user.getUserId());
		Profile saveProfile = profileRepository.save(basicProfile);
		User updateUser = user.updateProfile(saveProfile);
		userRepository.save(updateUser);

		Org org = makeOrgByCompanyAndDeptAndTeam(dto, saveProfile);

		return makeBasicInputResDto(saveProfile, user, org, updateUser.getUserId());
	}

	/**
	 * @param dto
	 * @param profile
	 * @return
	 * @회사, 부서, 팀 정보를 바탕으로 Org 생성
	 */

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
			.authRole(roleToClientRes(profile.getRole().name()))
			.isThreeMonth(checkHasItBeenThreeMonthsSinceTheLastPasswordChange(user))
			.build();
	}

}