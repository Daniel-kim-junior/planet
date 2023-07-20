package rocket.planet.controller.stats;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.jayway.jsonpath.JsonPath;

import rocket.planet.WithPlanetTestUser;

@SpringBootTest
class StatsControllerTest {

	@Autowired
	private StatsController statsController;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() throws Exception {

		mockMvc = MockMvcBuilders.standaloneSetup(statsController).build();
	}

	@Test
	@WithPlanetTestUser(email = "now20412041@gmail.com", role = "ROLE_ADMIN")
	void 전체_통계_확인_api_테스트() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/stats/entire?companyName=dktechin&unit=5")
				.characterEncoding("UTF-8"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType("application/json"))
			.andReturn();

		String rstJson = JsonPath.parse(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")))
			.read("$[0].name");
		assertThat(rstJson).isEqualTo("부문별");

		rstJson = JsonPath.parse(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")))
			.read("$[1].name");
		assertThat(rstJson).isEqualTo("기술별");

		rstJson = JsonPath.parse(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")))
			.read("$[2].name");
		assertThat(rstJson).isEqualTo("경력별");

		rstJson = JsonPath.parse(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")))
			.read("$[3].name");
		assertThat(rstJson).isEqualTo("팀별");

		rstJson = JsonPath.parse(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")))
			.read("$[4].name");
		assertThat(rstJson).isEqualTo("프로젝트별");

		rstJson = JsonPath.parse(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")))
			.read("$[5].name");
		assertThat(rstJson).isEqualTo("프로젝트 참여도");
	}

	@Test
	@WithPlanetTestUser(email = "now20412041@gmail.com", role = "ROLE_CAPTAIN")
	void 부문_통계_확인_api_테스트() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/stats/dept?deptName=스마트솔루션&unit=5").characterEncoding("UTF-8"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType("application/json"))
			.andReturn();

		String rstJson = JsonPath.parse(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")))
			.read("$[0].name");
		assertThat(rstJson).isEqualTo("기술별");

		rstJson = JsonPath.parse(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")))
			.read("$[1].name");
		assertThat(rstJson).isEqualTo("경력별");

		rstJson = JsonPath.parse(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")))
			.read("$[2].name");
		assertThat(rstJson).isEqualTo("팀별");

		rstJson = JsonPath.parse(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")))
			.read("$[3].name");
		assertThat(rstJson).isEqualTo("프로젝트별");

		rstJson = JsonPath.parse(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")))
			.read("$[4].name");
		assertThat(rstJson).isEqualTo("프로젝트 참여도");
	}

	@Test
	@WithPlanetTestUser(email = "now20412041@gmail.com", role = "ROLE_PILOT")
	void 팀_통계_확인_api_테스트() throws Exception {
		MvcResult mvcResult = mockMvc.perform(
				get("/api/stats/team?deptName=스마트솔루션&teamName=스마트팩토리&unit=5").characterEncoding("UTF-8"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType("application/json"))
			.andReturn();

		String rstJson = JsonPath.parse(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")))
			.read("$[0].name");
		assertThat(rstJson).isEqualTo("기술별");

		rstJson = JsonPath.parse(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")))
			.read("$[1].name");
		assertThat(rstJson).isEqualTo("경력별");

		rstJson = JsonPath.parse(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")))
			.read("$[2].name");
		assertThat(rstJson).isEqualTo("프로젝트별");

		rstJson = JsonPath.parse(mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8")))
			.read("$[3].name");
		assertThat(rstJson).isEqualTo("프로젝트 참여도");

	}

}