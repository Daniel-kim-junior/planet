package rocket.planet.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.dto.AdminTempDto;
import rocket.planet.service.admin.AdminDeptService;

@RestController
@RequestMapping("/api/admin/dept")
@RequiredArgsConstructor
public class AdminDeptController {

	private final AdminDeptService adminDeptService;

	@PostMapping
	public ResponseEntity<AdminTempDto.AdminResDto> deptAdd(AdminTempDto.AdminReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminDeptService.addDept(dto));
	}

	@DeleteMapping
	public ResponseEntity<AdminTempDto.AdminResDto> deptRemove(AdminTempDto.AdminReqDto dto) throws Exception {
		return ResponseEntity.ok().body(adminDeptService.removeDept(dto));
	}

	@PutMapping("/name")
	public ResponseEntity<AdminTempDto.AdminResDto> deptModify(AdminTempDto.AdminReqDto dto) {
		return ResponseEntity.ok().body(adminDeptService.modifyDept(dto));
	}
}
