package rocket.planet.controller.team;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import rocket.planet.dto.team.TeamMemberResDto;
import rocket.planet.service.team.TeamService;

@SpringBootTest
class TeamControllerTest {

	@Autowired
	private TeamService teamService;

	@Test
	void 팀멤버_조회_테스트() {
		String teamName = "스마트시티";
		List<TeamMemberResDto> teamMembers = teamService.getMemberList(teamName);
		System.out.println(teamMembers.toString());

	}
}