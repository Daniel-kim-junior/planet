package rocket.planet.controller.auth;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.TeamRepository;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class JoinFormController {

	private final DeptRepository deptRepository;

	private final TeamRepository teamRepository;

	@GetMapping("/join-dept")
	public List<String> joinFormDeptList() {
		return deptRepository.findDeptNameAll();
	}

	@GetMapping("/join-team")
	public List<String> joinFormTeamList(String deptName) {
		return teamRepository.findTeamNameByDeptName(deptName).stream()
			.map(e -> e.getTeamName()).collect(Collectors.toList());
	}

}
