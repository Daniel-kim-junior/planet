package rocket.planet.controller.stats;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.dto.stats.DeptStatsReqDto;
import rocket.planet.dto.stats.TeamStatsReqDto;
import rocket.planet.service.stats.StatsService;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

	private final StatsService adminRadarService;

	@GetMapping("/dept")
	public ResponseEntity deptStatsList(DeptStatsReqDto dto) {
		// 부문이 개발인지 비 개발인지에 따라 다른 로직을 수행한다.
		return ResponseEntity.ok().body(adminRadarService.getDeptStats(dto));
	}

	@GetMapping("/team")
	public String teamStatsList(@RequestParam TeamStatsReqDto teamStatsDto) {
		// 팀이 개발인지 비 개발인지에 따라 다른 로직을 수행한다.
		return null;
	}
}
