package rocket.planet.controller.stats;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import rocket.planet.WithPlanetTestUser;
import rocket.planet.service.stats.StatsService;

@SpringBootTest
class StatsControllerTest {

	@Autowired
	private StatsController statsController;

	@Autowired
	private StatsService statsService;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() throws Exception {

		mockMvc = MockMvcBuilders.standaloneSetup(statsController).build();
	}

	@Test
	@WithPlanetTestUser(email = "now20412041@gmail.com", role = "ROLE_ADMIN")
	void 전체_통계_확인_api_테스트() throws Exception {
		mockMvc.perform(get("/api/stats/entire?companyName=dktechin&unit=5"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.").exists());
	}

	@Test
	@WithPlanetTestUser(email = "now20412041@gmail.com", role = "ROLE_CAPTAIN")
	void 부문_통계_확인_api_테스트() throws Exception {
		mockMvc.perform(get("/api/stats/dept?deptName=스마트솔루션&unit=5"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType("application/json"));
	}

}