package rocket.planet.service.auth;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static rocket.planet.dto.auth.AuthDto.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

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
import rocket.planet.domain.redis.RefreshToken;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.repository.redis.AccessTokenRedisRepository;
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

@SpringBootTest
class AuthLoginAndJoinServiceTest {

	@InjectMocks
	private AuthLoginAndJoinService authLoginAndJoinService;

	@Mock
	private RefreshTokenRedisRepository refreshTokenRedisRepository;

	@Mock
	private AccessTokenRedisRepository accessTokenRedisRepository;

	@Mock
	private LastLoginRepository lastLoginRepository;

	@Mock
	private LimitLoginRepository limitLoginRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private JsonWebTokenIssuer jwtWebTokenIssuer;

	@Mock
	private EmailJoinConfirmRepository emailJoinConfirmRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	private String secretKey = "4GizURzFENPe76rDFNhLuUbE0QnPKmmdZT2Y5JWc+dQiWguHh8JxtI/7wqLywTGeiKar25HQnlfBJzA5Z4lTTw";

	private String token;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		final User adminUser = User.builder()
			.userPwd("encodedPassword")
			.userLock(false)
			.lastPwdModifiedDt(LocalDate.now())
			.userId("admin@gmail.com")
			.build();

		final User inValidUser = User.builder()
			.userPwd("encodedPassword")
			.userLock(false)
			.lastPwdModifiedDt(LocalDate.now())
			.userId("limit@gmail.com")
			.build();

		final Profile profile = Profile.builder().userId("limit@gmail.com").role(Role.ADMIN).build();
		profile.getOrg()
			.add(Org.builder().company(Company.builder().build()).department(Department.builder().build()).team(
				Team.builder().build()).build());

		when(userRepository.findByUserId("now204122@gmail.com")).thenReturn(Optional.empty());
		when(userRepository.findByUserId("admin@gmail.com")).thenReturn(Optional.of(adminUser));
		when(passwordEncoder.matches("encodedPassword", "encodedPassword")).thenReturn(true);
		when(passwordEncoder.matches("encoded222sdf", "encodedPassword")).thenReturn(false);
		when(limitLoginRepository.findById("admin@gmail.com")).thenReturn(Optional.empty());
		when(userRepository.findByUserId("limit@gmail.com")).thenReturn(Optional.of(inValidUser));
		when(limitLoginRepository.findById("limit@gmail.com"))
			.thenReturn(Optional.of(LimitLogin.builder().email("limit@gmail.com").count(4).build()));
		when(lastLoginRepository.findById("limit@gmail.com"))
			.thenReturn(Optional.of(LastLogin.builder().email("").build()));
		when(emailJoinConfirmRepository.findById("test@gmail.com")).thenReturn(Optional.empty());
		when(emailJoinConfirmRepository.findById("admin@gmail.com")).thenReturn(
			Optional.of(EmailJoinConfirm.builder().build()));

		/**
		 * jwt 토큰 테스트
		 */
		when(jwtWebTokenIssuer.createToken("test", Role.CREW.name(), secretKey.getBytes(), 10000))
			.thenCallRealMethod();
		when(refreshTokenRedisRepository.findById("test")).thenReturn(null);

		when(jwtWebTokenIssuer.createToken("success", Role.CREW.name(), secretKey.getBytes(),
			10000)).thenCallRealMethod();
		when(refreshTokenRedisRepository.findById("success")).thenReturn(
			Optional.of(RefreshToken.builder().token(token).email("success").build()));
	}

	@Test
	void 로그인시_존재하는_아이디인지_확인() throws Exception {
		assertThrows(UsernameNotFoundException.class, () -> {
			authLoginAndJoinService.checkLogin(LoginReqDto.builder()
				.id("now204122@gmail.com")
				.password("qwer1234!")
				.build());
		});
	}

	@Test
	void 로그인시_아이디가_존재할때_비밀번호_체크() throws Exception {
		final Optional<User> admin = userRepository.findByUserId("admin@gmail.com");
		final Optional<User> invalid = userRepository.findByUserId("limit@gmail.com");

		/**
		 * 비밀번호가 맞았을때
		 */
		assertDoesNotThrow(() -> {
			authLoginAndJoinService.checkPasswordTryFiveValidation(LoginReqDto.builder()
				.id("admin@gmail.com").password("encodedPassword").build(), admin.get());
		});

		/**
		 * 비밀번호가 틀렸을때
		 */
		assertThrows(PasswordMismatchException.class, () -> {
			authLoginAndJoinService.checkPasswordTryFiveValidation(LoginReqDto.builder()
				.id("admin@gmail.com").password("encoded222sdf").build(), admin.get());
		});

		/**
		 * 비밀번호 레디스의 정보가 취소 횟수가 비어있을때
		 */
		final Optional<LimitLogin> limitLogin = limitLoginRepository.findById("admin@gmail.com");
		assertThat(limitLogin).isEmpty();

		/**
		 * 비밀번호가 틀렸을때 취소 횟수가 4일때 아이디 잠금
		 */
		assertThrows(Temp30MinuteLockException.class, () -> {
			authLoginAndJoinService.checkPasswordTryFiveValidation(LoginReqDto.builder()
				.id("limit@gmail.com").password("encodedPassw").build(), invalid.get());
		});

	}

	@Test
	void 로그인_DTO_생성시_프로필이_있을때_없을때_분기처리() throws Exception {

		/**
		 *  메소드 성공
		 */
		assertDoesNotThrow(() ->
			authLoginAndJoinService
				.completeLogin(User.builder().userId("limit@gmail.com")
					.lastPwdModifiedDt(LocalDate.now()).profile(null).build()));

		/**
		 * 현재 시간을 기준으로 3개월이 경과
		 */
		LoginResDto dto = authLoginAndJoinService
			.completeLogin(User.builder().userId("limit@gmail.com")
				.lastPwdModifiedDt(LocalDate.of(2023, 4, 16)).profile(null).build());

		assertThat(dto.isThreeMonth()).isTrue();

		/**
		 * 초기 역할은 CREW
		 */
		assertThat(dto.getAuthRole()).isEqualTo(Role.CREW.name());

		/**
		 * user Profile이 존재할때
		 */
		final Profile profile = Profile.builder().userId("limit@gmail.com").role(Role.ADMIN).build();
		profile.getOrg()
			.add(Org.builder().company(Company.builder().build()).department(Department.builder().build()).team(
				Team.builder().build()).build());
		dto = authLoginAndJoinService
			.completeLogin(User.builder().userId("limit@gmail.com")
				.lastPwdModifiedDt(LocalDate.of(2023, 4, 16))
				.profile(profile).build());
		assertThat(dto.getAuthRole()).isEqualTo(Role.ADMIN.name());

	}

	@Test
	void 회원_가입_테스트() throws Exception {
		/**
		 * 회원가입시 이메일 인증 토큰이 존재하지 않을때
		 */
		assertThrows(NoValidEmailTokenException.class, () -> {
			authLoginAndJoinService.checkJoin(JoinReqDto.builder().id("test@gmail.com").build());
		});
		/**
		 * 회원가입시 이메일 인증 토큰이 존재하고 존재하는 회원일때
		 */
		assertThrows(AlreadyExistsIdException.class, () -> {
			authLoginAndJoinService.checkJoin(JoinReqDto.builder().id("admin@gmail.com").build());
		});
	}

	@Test
	void 엑세스_토큰_재요청_테스트() throws Exception {
		/**
		 * 유효하지 않은 jwt 토큰일때
		 */
		assertThrows(JwtInvalidException.class, () -> {
			authLoginAndJoinService.makeReissue("Bearer sdidjfifd");
		});

		/**
		 * 실패 jwt 토큰
		 */
		token = jwtWebTokenIssuer.createToken("test", Role.CREW.name(), secretKey.getBytes(), 10000);

		/**
		 * 다른 곳에서 만든 jwt 토큰 예외 발생
		 */
		assertThrows(JwtInvalidException.class, () -> {
			authLoginAndJoinService.makeReissue("Bearer " + token);
		});

	}
}