package rocket.planet.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<AdminResDto> teamList(@RequestBody AdminTeamModReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminTeamService.modifyTeam(dto));
	}

	@PostMapping
	public ResponseEntity<AdminResDto> teamAdd(@RequestBody AdminTeamAddReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminTeamService.addTeam(dto));
	}

	@PatchMapping
	public ResponseEntity<AdminResDto> teamActiveModify(@RequestBody AdminDeptTeamDto.UpdateTeamActiveReqDto teamReqDto) {
		return ResponseEntity.ok().body(adminTeamService.modifyTeamActive(teamReqDto));
	}

}
