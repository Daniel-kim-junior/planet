package rocket.planet.controller.team;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.dto.team.TeamMemberResDto;
import rocket.planet.service.team.TeamService;

@RestController
@Slf4j
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {
	private final TeamService teamService;

	@GetMapping("/members")
	public ResponseEntity<List<TeamMemberResDto>> memberList(String teamName) {
		List<TeamMemberResDto> teamMembers = teamService.getMemberList(teamName);

		return ResponseEntity.ok().body(teamMembers);

	}
}
