package rocket.planet.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.nio.charset.StandardCharsets;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import rocket.planet.configuration.SecurityConfig;
import rocket.planet.controller.test.ExceptionController;
import rocket.planet.dto.login.LoginRequestDto;

/*
 * Controller Exception 테스트 (스프링 시큐리티 제외)
 */
@WebMvcTest(controllers = ExceptionController.class,
	excludeAutoConfiguration = SecurityAutoConfiguration.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@AutoConfigureMockMvc
class ControllerExceptionUnitTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@DisplayName("로그인 요청 실패 - 아이디 없음")
	@Test
	void 아이디_불일치_에러_핸들링() throws Exception {
		String jsonLoginReq = objectMapper.writeValueAsString(
			LoginRequestDto.builder().id("now2041@gmail.com").password("qqwe1234!").build());
		String expectedJson = "{\"success\":false,\"response\":null,\"error\":{\"code\":\"UE001\",\"message\":\"해당 아이디가 존재하지 않습니다\"}}";
		MvcResult mvcResult = mockMvc.perform(
				post("/test/error/id-mismatch").content(jsonLoginReq)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(
						StandardCharsets.UTF_8))
			.andExpect(result -> result.getResponse().setCharacterEncoding("UTF-8"))
			.andReturn();
		String result = mvcResult.getResponse().getContentAsString();
		Assertions.assertThat(result).isEqualTo(expectedJson);
	}

	@DisplayName("로그인 요청 실패 - 비밀번호 없음")
	@Test
	void 비밀번호_불일치_에러_핸들링() throws Exception {
		String jsonLoginReq = objectMapper.writeValueAsString(
			LoginRequestDto.builder().id("now2041@gmail.com").password("qqwe1234!").build());
		String expectedJson = "{\"success\":false,\"response\":null,\"error\":{\"code\":\"UE002\",\"message\":\"비밀번호가 일치하지 않습니다\"}}";
		MvcResult mvcResult = mockMvc.perform(
				post("/test/error/password-mismatch").content(jsonLoginReq)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(
						StandardCharsets.UTF_8))
			.andExpect(result -> result.getResponse().setCharacterEncoding("UTF-8"))
			.andReturn();
		String result = mvcResult.getResponse().getContentAsString();
		Assertions.assertThat(result).isEqualTo(expectedJson);
	}

	@DisplayName("이메일 형식 유효성 검사 실패")
	@Test
	void 이메일_형식_유효성_에러_핸들링() throws Exception {
		String jsonReq = objectMapper.writeValueAsString(
			LoginRequestDto.builder().id("now2041gmail.com").password("qqwe1234!").build());
		String expectedJson = "{\"success\":false,\"response\":null,\"error\":{\"code\":\"UE003\",\"message\":\"이메일 형식이 올바르지 않습니다\"}}";
		MvcResult mvcResult = mockMvc.perform(
				post("/test/error/user-data-invalid").content(jsonReq)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(
						StandardCharsets.UTF_8))
			.andExpect(result -> result.getResponse().setCharacterEncoding("UTF-8"))
			.andReturn();
		String result = mvcResult.getResponse().getContentAsString();
		Assertions.assertThat(result).isEqualTo(expectedJson);
	}

	@DisplayName("비밀번호 유효성 검사 실패")
	@Test
	void 비밀번호_유효성_검사_에러_핸들링() throws Exception {
		String jsonReq = objectMapper.writeValueAsString(
			LoginRequestDto.builder().id("now2041@gmail.com").password("qqwe123").build());
		String expectedJson = "{\"success\":false,\"response\":null,\"error\":{\"code\":\"UE004\",\"message\":\"8자 이상 16자 미만의 특수문자, 숫자, 영문자 조합이어야 합니다\"}}";

		MvcResult mvcResult = mockMvc.perform(
				post("/test/error/user-data-invalid").content(jsonReq)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(
						StandardCharsets.UTF_8))
			.andExpect(result -> result.getResponse().setCharacterEncoding("UTF-8"))
			.andReturn();

		String result = mvcResult.getResponse().getContentAsString();
		Assertions.assertThat(result).isEqualTo(expectedJson);
	}

}