package rocket.planet.controller.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rocket.planet.dto.auth.AuthDto.*;
import static rocket.planet.dto.auth.AuthDto.JoinReqDto.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class AuthRequestDtoTest {
	@Autowired
	private AuthController authController;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
	}

	@Test
	void dto_validation_체크_테스트() throws Exception {
		/**
		 * 비밀번호 유효성 실패 테스팅
		 */
		JoinReqDto dto = builder().id("test").password("failtest").build();
		objectMapper.writeValueAsString(dto);
		mockMvc.perform(post("/api/auth/account-register").contentType("application/json")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(dto)).characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());

		dto = builder().id("test").password("failtest!").build();
		objectMapper.writeValueAsString(dto);
		mockMvc.perform(post("/api/auth/account-register").contentType("application/json")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(dto)).characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());

		dto = builder().id("test").password("failtest1!").build();
		objectMapper.writeValueAsString(dto);
		mockMvc.perform(post("/api/auth/account-register").contentType("application/json")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(dto)).characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());

		dto = builder().id("test").password("failtes").build();
		objectMapper.writeValueAsString(dto);
		mockMvc.perform(post("/api/auth/account-register").contentType("application/json")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(dto)).characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());

		dto = builder().id(" ").password("failtest!").build();
		objectMapper.writeValueAsString(dto);
		mockMvc.perform(post("/api/auth/account-register").contentType("application/json")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(dto)).characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());
		/**
		 * Career가 0보다 작을때 실패
		 */
		BasicInputReqDto biDto = BasicInputReqDto.builder()
			.userName("dd")
			.career(-1)
			.deptName("dd")
			.teamName("dd")
			.build();
		objectMapper.writeValueAsString(dto);
		mockMvc.perform(post("/api/auth/basic-profile-register").contentType("application/json")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(biDto)).characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());
		/**
		 * teamName, userName, deptName이 공백일때 실패
		 */
		biDto = BasicInputReqDto.builder()
			.userName(" ")
			.career(1)
			.deptName("dd")
			.teamName("dd")
			.build();
		objectMapper.writeValueAsString(dto);
		mockMvc.perform(post("/api/auth/basic-profile-register").contentType("application/json")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(biDto)).characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());
		biDto = BasicInputReqDto.builder()
			.userName("dd")
			.career(1)
			.deptName(" ")
			.teamName("dd")
			.build();
		objectMapper.writeValueAsString(dto);
		mockMvc.perform(post("/api/auth/basic-profile-register").contentType("application/json")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(biDto)).characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());
		biDto = BasicInputReqDto.builder()
			.userName("dd")
			.career(1)
			.deptName("dd")
			.teamName(" ")
			.build();
		objectMapper.writeValueAsString(dto);
		mockMvc.perform(post("/api/auth/basic-profile-register").contentType("application/json")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(biDto)).characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());

		/**
		 * 로그인 id가 email 형식이 아닐때 실패
		 */
		LoginReqDto loginDto = LoginReqDto.builder()
			.id("now20412401").password("Qwer1234!").build();
		objectMapper.writeValueAsString(dto);
		mockMvc.perform(post("/api/auth/login").contentType("application/json")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(loginDto)).characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());
		/**
		 * 엑세스 토큰 재발급 요청에서 헤더에 토큰이 없을때 실패
		 */
		mockMvc.perform(post("/api/auth/reissue")
				.contentType("application/json")
				.characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());
		/**
		 * 아이디가 공백일때 실패
		 */
		loginDto = LoginReqDto.builder()
			.id(" ").password("Qwer1234!").build();
		objectMapper.writeValueAsString(dto);
		mockMvc.perform(post("/api/auth/login").contentType("application/json")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(loginDto)).characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());

	}
}
