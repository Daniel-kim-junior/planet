package rocket.planet.controller.team;

import static rocket.planet.dto.team.TeamMemberDto.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.dto.common.ListReqDto;
import rocket.planet.service.team.TeamService;

@RestController
@Slf4j
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {
	private final TeamService teamService;

	@GetMapping("/members")
	public ResponseEntity<TeamMemberListDto> memberList(@ModelAttribute ListReqDto listReqDto, String teamName) {
		TeamMemberListDto teamMembers = teamService.getMemberList(listReqDto, teamName);

		return ResponseEntity.ok().body(teamMembers);

	}
}
