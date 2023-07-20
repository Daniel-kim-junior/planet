package rocket.planet.controller.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import rocket.planet.WithPlanetTestUser;

@SpringBootTest
class JoinFormControllerTest {
	@Autowired
	private JoinFormController joinFormController;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() throws Exception {

		mockMvc = MockMvcBuilders.standaloneSetup(joinFormController).build();
	}

	@Test
	@WithPlanetTestUser(email = "now20412041@gmail.com", role = "ROLE_ANONYMOUS")
	void 부서_이름_리스트_정보_확인_테스트() throws Exception {
		mockMvc.perform(get("/api/auth/join-dept").characterEncoding("UTF-8"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType("application/json"))
			.andReturn();

	}
}