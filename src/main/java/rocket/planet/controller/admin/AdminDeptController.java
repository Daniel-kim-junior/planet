package rocket.planet.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/dept")
@RequiredArgsConstructor
public class AdminDeptController {

	private final AdminDeptService adminDeptService;

	@PostMapping
	public ResponseEntity<AdminDto.AdminResDto> deptAdd(AdminDto.AdminReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminDeptService.addDept(dto));
	}

	@DeleteMapping
	public ResponseEntity<AdminDto.AdminResDto> deptRemove(AdminDto.AdminReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminDeptService.removeDept(dto));
	}

	@PutMapping("/name")
	public ResponseEntity<AdminDto.AdminResDto> deptModify(AdminDto.AdminReqDto dto) {
		return ResponseEntity.ok().body(adminDeptService.modifyDept(dto));
	}
}
