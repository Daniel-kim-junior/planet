package rocket.planet.controller.admin;

import static rocket.planet.dto.admin.AdminDto.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.service.auth.AuthorityService;

@RestController
@Slf4j
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
	// 사용자 권한 변경

	private final AuthorityService authorityService;

	@PatchMapping("/user/auth")
	public ResponseEntity<String> authorityModify(AdminAuthModifyReqDto adminAuthModifyReqDto) {
		authorityService.modifyAuthority(adminAuthModifyReqDto);

		return ResponseEntity.ok().body(adminAuthModifyReqDto.getUserNickName() + "님의 권한이 수정되었습니다.");
	}

	@GetMapping("/user/auth")
	public ResponseEntity<List<AdminAuthMemberResDto>> adminTeamMemberList(String teamName) {
		List<AdminAuthMemberResDto> teamMemberList = authorityService.getTeamMemberList(teamName);

		return ResponseEntity.ok().body(teamMemberList);

	}
}
