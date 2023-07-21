package rocket.planet.controller.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import rocket.planet.dto.auth.AuthDto.PasswordModifyReqDto;

@SpringBootTest
class FindPasswordRequestDtoTest {

	@Autowired
	private FindPasswordController findPasswordController;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(findPasswordController).build();
	}

	@Test
	void dto_validation_체크_테스트() throws Exception {
		/**
		 * 비밀번호 유효성 및 id 실패 테스팅
		 */
		PasswordModifyReqDto dto = PasswordModifyReqDto.builder().id("test").password("failtest").build();
		String loginDto = objectMapper.writeValueAsString(dto);

		mockMvc.perform(patch("/api/auth/password/modify").contentType("application/json")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(loginDto)).characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());

		dto = PasswordModifyReqDto.builder().id(" ").password("FailTest!2").build();
		loginDto = objectMapper.writeValueAsString(dto);

		mockMvc.perform(patch("/api/auth/password/modify").contentType("application/json")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(loginDto)).characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());

		dto = PasswordModifyReqDto.builder().id("now20").password("FailTest!2").build();
		loginDto = objectMapper.writeValueAsString(dto);

		mockMvc.perform(patch("/api/auth/password/modify").contentType("application/json")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(loginDto)).characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());
	}

}