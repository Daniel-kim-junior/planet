package rocket.planet.controller.admin;

import static rocket.planet.dto.admin.AdminDeptTeamDto.*;

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
import rocket.planet.service.admin.AdminDeptService;

@RestController
@RequestMapping("/api/admin/dept")
@RequiredArgsConstructor
public class AdminDeptController {

	private final AdminDeptService adminDeptService;

	@PostMapping
	public ResponseEntity<AdminResDto> deptAdd(@RequestBody @Valid AdminDeptAddReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminDeptService.addDept(dto));
	}

	@PutMapping("/name")
	public ResponseEntity<AdminResDto> deptModify(@RequestBody AdminDeptModReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminDeptService.modifyDept(dto));
	}

	@PatchMapping
	public ResponseEntity<AdminResDto> teamActiveModify(
		@RequestBody AdminDeptTeamDto.UpdateDeptActiveReqDto deptReqDto) {
		return ResponseEntity.ok().body(adminDeptService.modifyDeptActive(deptReqDto));
	}
}
