package rocket.planet.controller.admin;

import static rocket.planet.dto.admin.AdminDeptTeamDto.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.service.admin.AdminDeptService;

@RestController
@RequestMapping("/api/admin/dept")
@RequiredArgsConstructor
public class AdminDeptController {

	private final AdminDeptService adminDeptService;

	@PostMapping
	public ResponseEntity<AdminResDto> deptAdd(AdminDeptAddReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminDeptService.addDept(dto));
	}

	@DeleteMapping
	public ResponseEntity<AdminResDto> deptRemove(AdminDeptTeamDelReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminDeptService.removeDept(dto));
	}

	@PutMapping("/name")
	public ResponseEntity<AdminResDto> deptModify(
		AdminDeptModReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminDeptService.modifyDept(dto));
	}
}
