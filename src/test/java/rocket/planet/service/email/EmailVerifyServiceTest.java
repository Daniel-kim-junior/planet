package rocket.planet.service.email;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import rocket.planet.domain.User;
import rocket.planet.dto.email.EmailDto;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.repository.redis.EmailFindConfirmRepository;
import rocket.planet.repository.redis.EmailFindTokenRepository;
import rocket.planet.repository.redis.EmailJoinConfirmRepository;
import rocket.planet.repository.redis.EmailJoinTokenRepository;
import rocket.planet.util.exception.AlreadyExistsIdException;

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
		when(userRepository.findByUserId("test1")).thenReturn(Optional.of(User.builder().build()));

	}

	@Test
	void 회원가입_인증_이메일_전송_시_유저가_존재할때() throws Exception {
		Assertions.assertThrows(AlreadyExistsIdException.class, () -> {
			emailVerifyService.saveLimitTimeAndSendEmail(EmailDto.
				EmailDuplicateCheckAndSendEmailReqDto.builder().type("join")
				.id("test1")
				.build());
		});

	}

}