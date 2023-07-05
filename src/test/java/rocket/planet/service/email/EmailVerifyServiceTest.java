package rocket.planet.service.email;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.nio.charset.StandardCharsets;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class EmailVerifyServiceTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@DisplayName("로그인 요청 실패 - 아이디 없음")
	@Test
	void 아이디_불일치_에러_핸들링() throws Exception {

		String expectedJson = "{\"success\":false,\"response\":null,\"error\":{\"code\":\"UE005\",\"message\":\"해당 이메일이 존재하지 않습니다\"}}";
		
		MvcResult mvcResult = mockMvc.perform(
				get("/api/email-verify?email=gmail@gmail.com")
					.characterEncoding(
						StandardCharsets.UTF_8))
			.andExpect(result -> result.getResponse().setCharacterEncoding("UTF-8"))
			.andReturn();
		String result = mvcResult.getResponse().getContentAsString();
		Assertions.assertThat(result).isEqualTo(expectedJson);
	}

}