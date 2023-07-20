package rocket.planet.service.email;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.repository.redis.EmailFindConfirmRepository;
import rocket.planet.repository.redis.EmailFindTokenRepository;
import rocket.planet.repository.redis.EmailJoinConfirmRepository;
import rocket.planet.repository.redis.EmailJoinTokenRepository;

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

	}

	@DisplayName("메일 인증 서비스 - 랜덤 문자열 추출")
	@Test
	void 인증_서비스_랜덤_문자열_추출() throws Exception {

	}

	@DisplayName("메일 인증 서비스 - 컨트롤러 테스트")
	@Test
	void 인증_서비스_컨트롤러_테스트() throws Exception {

	}

}