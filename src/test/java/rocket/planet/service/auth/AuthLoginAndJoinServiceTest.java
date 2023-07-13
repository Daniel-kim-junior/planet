package rocket.planet.service.auth;

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
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.domain.User;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.repository.redis.AuthChangeRepository;
import rocket.planet.repository.redis.LastLoginRepository;
import rocket.planet.repository.redis.LimitLoginRepository;
import rocket.planet.repository.redis.RefreshTokenRedisRepository;
import rocket.planet.util.exception.PasswordMismatchException;

@SpringBootTest
@Transactional
class AuthLoginAndJoinServiceTest {

	@InjectMocks
	private AuthLoginAndJoinService authLoginAndJoinService;

	@Mock
	private RefreshTokenRedisRepository refreshTokenRedisRepository;

	@Mock
	private AuthChangeRepository authChangeRepository;

	@Mock
	private LastLoginRepository lastLoginRepository;

	@Mock
	private LimitLoginRepository limitLoginRepository;

	@Mock
	private UserRepository userRepository;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		User adminUser = User.builder()
			.userPwd("encodedPassword")
			.userLock(false)
			.lastPwdModifiedDt(LocalDate.now())
			.userId("admin@gmail.com")
			.build();

		// when(userRepository.findByUserId("now204122@gmail.com")).thenReturn(Optional.empty());
		when(userRepository.findByUserId("admin@gmail.com")).thenReturn(Optional.of(adminUser));

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
		Optional<User> save = userRepository.findByUserId("admin@gmail.com");
		/**
		 * 비밀번호가 맞았을때
		 */
		assertDoesNotThrow(() -> {
			authLoginAndJoinService.checkPasswordTryFiveValidation(LoginReqDto.builder()
				.id("admin@gmail.com").password("encodedPassword").build(), save.get());
		});

		/**
		 * 비밀번호가 처음 틀렸을때
		 */
		assertThrows(PasswordMismatchException.class, () -> {
			authLoginAndJoinService.checkPasswordTryFiveValidation(LoginReqDto.builder()
				.id("admin@gmail.com").password("encoded222sdf").build(), save.get());
		});
		//
		// /**
		//  * 비밀번호가 틀렸을때 레디스의 정보가 취소 횟수가 비어있을때
		//  */
		// Optional<LimitLogin> limitLogin = limitLoginRepository.findById("admin@gmail.com");
		// assertThat(limitLogin).isEmpty();
	}

}