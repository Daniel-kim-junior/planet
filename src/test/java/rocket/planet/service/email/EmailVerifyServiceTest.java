package rocket.planet.service.email;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import rocket.planet.domain.User;
import rocket.planet.domain.redis.EmailFindToken;
import rocket.planet.domain.redis.EmailJoinToken;
import rocket.planet.dto.email.EmailDto;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.repository.redis.EmailFindConfirmRepository;
import rocket.planet.repository.redis.EmailFindTokenRepository;
import rocket.planet.repository.redis.EmailJoinConfirmRepository;
import rocket.planet.repository.redis.EmailJoinTokenRepository;
import rocket.planet.util.exception.NoValidEmailTokenException;

@SpringBootTest
class EmailVerifyServiceTest {

	@InjectMocks
	private EmailVerifyService emailVerifyService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private JavaMailSender mailSender;

	@Mock
	private EmailJoinTokenRepository emailJoinTokenRepository;

	@Mock
	private EmailJoinConfirmRepository emailJoinConfirmRepository;

	@Mock
	private EmailFindTokenRepository emailFindTokenRepository;

	@Mock
	private EmailFindConfirmRepository emailFindConfirmRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		/**
		 * test1 유저가 존재할때 가입 불가
		 */
		when(userRepository.findByUserId("test1")).thenReturn(Optional.of(mock(User.class)));
		/**
		 * test2 유저가 존재gk
		 */
		when(userRepository.findByUserId("test2")).thenReturn(Optional.empty());
		/**
		 * test3 유저가 존재하지 않을때
		 */
		when(userRepository.findByUserId("test3")).thenReturn(Optional.empty());

		/**
		 * 회원 가입 토큰이 존재하지 않을때
		 */
		when(emailJoinTokenRepository.findById("test2")).thenReturn(Optional.empty());
		/**
		 * 회원 가입 토큰이 존재할때
		 */
		when(emailJoinTokenRepository.findById("test3")).thenReturn(
			Optional.of(EmailJoinToken.builder().email("test3").token("test3").build()));

		/**
		 * 비밀번호 찾기 토큰이 존재하지 않을때
		 */
		when(emailFindTokenRepository.findById("test2")).thenReturn(Optional.empty());
		/**
		 * 비밀번호 찾기 토큰이 존재할때
		 */
		when(emailFindTokenRepository.findById("test3")).thenReturn(
			Optional.of(EmailFindToken.builder().email("test3").token("test3").build()));
	}

	@Test
	void 회원가입_인증_이메일_전송_시_유저가_존재할때() throws Exception {

		final CompletableFuture<String> future1 = emailVerifyService.saveLimitTimeAndSendEmail(
			EmailDto.EmailDuplicateCheckAndSendEmailReqDto
				.builder().id("test1").type("join").build());
		/**
		 * 비동기 메소드 id 존재 예외
		 */
		assertThrows(ExecutionException.class, () -> {
			future1.get();
		});

	}

	@Test
	void 회원가입_인증번호_확인() throws Exception {

		assertThrows(NoValidEmailTokenException.class, () -> {
			emailVerifyService.checkByRedisEmailTokenAndSaveToken("test1", "qwer2", "join");
		});

		assertThrows(NoValidEmailTokenException.class, () -> {
			emailVerifyService.checkByRedisEmailTokenAndSaveToken("test2", "test2", "join");
		});

		assertDoesNotThrow(() -> {
			emailVerifyService.checkByRedisEmailTokenAndSaveToken("test3", "test3", "join").getMessage();
		});

		final String message = emailVerifyService.checkByRedisEmailTokenAndSaveToken("test3", "test3", "join")
			.getMessage();
		assertThat(message).isEqualTo("ROCKET PLANET 이메일 인증이 완료되었습니다.");
	}

	@Test
	void 비밀번호_찾기_인증번호_확인() throws Exception {
		assertThrows(NoValidEmailTokenException.class, () -> {
			emailVerifyService.checkByRedisEmailTokenAndSaveToken("test1", "qwer2", "find");
		});

		assertThrows(NoValidEmailTokenException.class, () -> {
			emailVerifyService.checkByRedisEmailTokenAndSaveToken("test2", "test2", "find");
		});

		assertDoesNotThrow(() -> {
			emailVerifyService.checkByRedisEmailTokenAndSaveToken("test3", "test3", "find").getMessage();
		});

		final String message = emailVerifyService.checkByRedisEmailTokenAndSaveToken("test3", "test3", "find")
			.getMessage();
		assertThat(message).isEqualTo("ROCKET PLANET 이메일 인증이 완료되었습니다.");
	}

	@Test
	void 이메일_인증번호_랜덤_문자_생성_테스트() {
		final String s = emailVerifyService.makeRandomString(6);
		assertThat(s.length()).isEqualTo(6);
	}

}