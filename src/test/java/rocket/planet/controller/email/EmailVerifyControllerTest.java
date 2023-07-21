package rocket.planet.controller.email;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.Charset;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import rocket.planet.WithPlanetTestUser;
import rocket.planet.dto.email.EmailDto;

@SpringBootTest
class EmailVerifyControllerTest {
	@Autowired
	private EmailVerifyController emailVerifyController;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() throws Exception {

		mockMvc = MockMvcBuilders.standaloneSetup(emailVerifyController).build();
	}

	@Test
	@WithPlanetTestUser(email = "now20412041@gmail.com", role = "ROLE_ANONYMOUS")
	void 이메일_인증_테스트() throws Exception {
		EmailDto.EmailDuplicateCheckAndSendEmailReqDto dto = EmailDto.EmailDuplicateCheckAndSendEmailReqDto
			.builder().id("now20412041@gmail.com").type("join")
			.build();
		String content = objectMapper.writeValueAsString(dto);
		MvcResult mvcResult = mockMvc.perform(post("/api/auth/email-verify").characterEncoding("UTF-8")
				.content(content).contentType("application/json"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType("application/json"))
			.andReturn();

		Object read = JsonPath.parse(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")))
			.read("$.message");

		Assertions.assertThat(read.toString()).isEqualTo("이메일 인증번호 전송을 완료했습니다");
	}

	@Test
	void request_dto_테스트() throws Exception {
		EmailDto.EmailDuplicateCheckAndSendEmailReqDto dto = EmailDto.EmailDuplicateCheckAndSendEmailReqDto
			.builder().id(" ").type("join").build();
		String content = objectMapper.writeValueAsString(dto);

		mockMvc.perform(post("/api/auth/email-verify").characterEncoding("UTF-8")
				.content(content).contentType("application/json"))
			.andExpect(status().isBadRequest());
		dto = EmailDto.EmailDuplicateCheckAndSendEmailReqDto
			.builder().id("now20412").type("join").build();
		content = objectMapper.writeValueAsString(dto);

		mockMvc.perform(post("/api/auth/email-verify").characterEncoding("UTF-8")
				.content(content).contentType("application/json"))
			.andExpect(status().isBadRequest());

		dto = EmailDto.EmailDuplicateCheckAndSendEmailReqDto
			.builder().id("now20412051@gmail.com").type("jo").build();
		content = objectMapper.writeValueAsString(dto);

		mockMvc.perform(post("/api/auth/email-verify").characterEncoding("UTF-8")
				.content(content).contentType("application/json"))
			.andExpect(status().isBadRequest());

		dto = EmailDto.EmailDuplicateCheckAndSendEmailReqDto
			.builder().id("now20412051@gmail.com").type(" ").build();
		content = objectMapper.writeValueAsString(dto);

		mockMvc.perform(post("/api/auth/email-verify").characterEncoding("UTF-8")
				.content(content).contentType("application/json"))
			.andExpect(status().isBadRequest());
	}
}