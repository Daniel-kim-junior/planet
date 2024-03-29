package rocket.planet.controller.admin;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.dto.admin.AdminDeptTeamDto;
import rocket.planet.dto.admin.AdminDeptTeamDto.AdminResDto;
import rocket.planet.dto.admin.AdminDeptTeamDto.AdminTeamAddReqDto;
import rocket.planet.dto.admin.AdminDeptTeamDto.AdminTeamModReqDto;
import rocket.planet.service.admin.AdminTeamService;

@RestController
@RequestMapping("/api/admin/team")
@RequiredArgsConstructor
public class AdminTeamController {

	private final AdminTeamService adminTeamService;

	@PutMapping("/name")
	public ResponseEntity<AdminResDto> teamModify(@Valid @RequestBody AdminTeamModReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminTeamService.modifyTeam(dto));
	}

	@PostMapping
	public ResponseEntity<AdminResDto> teamAdd(@Valid @RequestBody AdminTeamAddReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminTeamService.addTeam(dto));
	}

	@PatchMapping
	public ResponseEntity<AdminResDto> teamActiveModify(
		@RequestBody AdminDeptTeamDto.UpdateTeamActiveReqDto teamReqDto) {
		return ResponseEntity.ok().body(adminTeamService.modifyTeamActive(teamReqDto));
	}

}
