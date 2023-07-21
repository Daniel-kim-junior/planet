package rocket.planet.controller.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rocket.planet.dto.admin.AdminDeptTeamDto.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import rocket.planet.WithPlanetTestUser;
import rocket.planet.controller.admin.AdminDeptController;

@SpringBootTest
public class AdminDeptRequestDtoTest {

	private MockMvc mockMvc;

	@Autowired
	private AdminDeptController adminDeptController;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() throws Exception {

		mockMvc = MockMvcBuilders.standaloneSetup(adminDeptController).build();

	}

	@Test
	@WithPlanetTestUser(email = "now20412041@gmail.com", role = "ROLE_ADMIN")
	void dto_request_validation_실패_테스트() throws Exception {
		AdminDeptAddReqDto dto = AdminDeptAddReqDto.builder()
			.deptType("DEV").name("gg").build();
		String test = objectMapper.writeValueAsString(dto);
		mockMvc.perform(post("/api/admin/dept").contentType("application/json")
				.content(test)
				.characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());

		dto = AdminDeptAddReqDto.builder()
			.deptType("DEVELOPMENT").name(" ").build();
		test = objectMapper.writeValueAsString(dto);
		mockMvc.perform(post("/api/admin/dept").contentType("application/json")
				.content(test)
				.characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());

		AdminDeptModReqDto modDto = AdminDeptModReqDto.builder()
			.changeName(" ").targetName("dsddf").build();
		test = objectMapper.writeValueAsString(modDto);
		mockMvc.perform(put("/api/admin/dept/name").contentType("application/json")
				.content(test)
				.characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());

		modDto = AdminDeptModReqDto.builder()
			.changeName("dsfds").targetName(" ").build();
		test = objectMapper.writeValueAsString(modDto);
		mockMvc.perform(put("/api/admin/dept/name").contentType("application/json")
				.content(test)
				.characterEncoding("UTF-8"))
			.andExpect(status().isBadRequest());

	}

}
