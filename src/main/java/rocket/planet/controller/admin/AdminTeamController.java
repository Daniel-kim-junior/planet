package rocket.planet.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.dto.admin.AdminDeptTeamDto.AdminDeptTeamDelReqDto;
import rocket.planet.dto.admin.AdminDeptTeamDto.AdminResDto;
import rocket.planet.dto.admin.AdminDeptTeamDto.AdminTeamAddReqDto;
import rocket.planet.dto.admin.AdminDeptTeamDto.AdminTeamModReqDto;
import rocket.planet.service.admin.AdminTeamService;

@RestController
@RequestMapping("/api/admin/team")
@RequiredArgsConstructor
public class AdminTeamController {

	private final AdminTeamService adminTeamService;

	@DeleteMapping
	public ResponseEntity<AdminResDto> teamDelete(AdminDeptTeamDelReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminTeamService.removeTeam(dto));
	}

	@PutMapping("/name")
	public ResponseEntity<AdminResDto> teamList(AdminTeamModReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminTeamService.modifyTeam(dto));
	}

	@PostMapping
	public ResponseEntity<AdminResDto> teamAdd(AdminTeamAddReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminTeamService.addTeam(dto));
	}

}