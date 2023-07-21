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

@SpringBootTest
class LogOutRequestDtoTest {

	@Autowired
	private LogOutController logOutController;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(logOutController).build();
	}

	@Test
	void dto_validation_체크_테스트() throws Exception {

		/**
		 * 로그아웃 시 헤더에 토큰이 존재 x
		 */

		mockMvc.perform(post("/api/logout")).andExpect(status().isBadRequest());

	}

}