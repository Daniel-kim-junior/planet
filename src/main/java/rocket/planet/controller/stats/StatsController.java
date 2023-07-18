package rocket.planet.controller.stats;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.dto.stats.DeptStatsReqDto;
import rocket.planet.dto.stats.EntireStatsReqDto;
import rocket.planet.dto.stats.TeamStatsReqDto;
import rocket.planet.service.stats.StatsService;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

	private final StatsService adminRadarService;

	@GetMapping("/dept")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADAR', 'ROLE_CAPTAIN')")
	public ResponseEntity<?> deptStatsList(DeptStatsReqDto dto) {
		// 부문이 개발인지 비 개발인지에 따라 다른 로직을 수행한다.
		return ResponseEntity.ok().body(adminRadarService.getDeptStats(dto));
	}

	@GetMapping("/team")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADAR', 'ROLE_CAPTAIN', 'ROLE_PILOT')")
	public ResponseEntity<?> teamStatsList(TeamStatsReqDto dto) {
		// 팀이 개발인지 비 개발인지에 따라 다른 로직을 수행한다.
		return ResponseEntity.ok().body(adminRadarService.getTeamStats(dto));
	}

	@GetMapping("/entire")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADAR')")
	public ResponseEntity<?> entireStatsList(EntireStatsReqDto dto) {
		return ResponseEntity.ok().body(adminRadarService.getEntireStats(dto));
	}
}
