package rocket.planet.controller.team;

import static rocket.planet.dto.team.TeamMemberDto.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.dto.common.ListReqDto;
import rocket.planet.service.team.TeamService;

@SpringBootTest
class TeamControllerTest {

	@Autowired
	private TeamService teamService;

	@Test
	@Transactional
	void 팀멤버_조회_테스트() {
		String teamName = "인사";
		ListReqDto listReqDto = ListReqDto.builder()
			.page(1)
			.pageSize(8)
			.build();

		TeamMemberListDto teamMembers = teamService.getMemberList(listReqDto, teamName);

		// List<TeamMemberResDto> teamMembers = teamService.getMemberList(teamName);
		// System.out.println(teamMembers.toString());
		System.out.println(teamMembers);
	}
}