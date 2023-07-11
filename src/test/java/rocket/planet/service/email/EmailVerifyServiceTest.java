package rocket.planet.service.email;

import java.util.concurrent.CompletableFuture;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class EmailVerifyServiceTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private EmailVerifyService emailVerifyService;
	@Autowired
	private ObjectMapper objectMapper;

	@DisplayName("메일 인증 서비스 - 랜덤 문자열 추출")
	@Test
	void 인증_서비스_랜덤_문자열_추출() throws Exception {
		CompletableFuture<String> future = emailVerifyService.saveLimitTimeAndSendEmail("test20412041@gmail.com");
		String result = future.join();
		Assertions.assertThat(result.length()).isEqualTo(6);
	}

	@DisplayName("메일 인증 서비스 - 컨트롤러 테스트")
	@Test
	void 인증_서비스_컨트롤러_테스트() throws Exception {
		// emailVerifyService.redisSaveToken("test20412041@gmail.com",
		// 	"123456");

	}
}