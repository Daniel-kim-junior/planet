package rocket.planet.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.dto.AdminTempDto.AdminReqDto;
import rocket.planet.dto.AdminTempDto.AdminResDto;
import rocket.planet.service.admin.AdminTeamService;

@RestController
@RequestMapping("/api/admin/team")
@RequiredArgsConstructor
public class AdminTeamController {

	private final AdminTeamService adminTeamService;

	@DeleteMapping
	public ResponseEntity<AdminResDto> teamDelete(AdminReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminTeamService.removeTeam(dto));
	}

	@PutMapping("/name")
	public ResponseEntity<AdminResDto> teamList(AdminReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminTeamService.modifyTeam(dto));
	}

	@PostMapping
	public ResponseEntity<AdminResDto> teamCreate(AdminReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminTeamService.addTeam(dto));
	}

}
