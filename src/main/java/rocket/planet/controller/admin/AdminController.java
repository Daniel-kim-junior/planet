package rocket.planet.controller.admin;

import static rocket.planet.dto.admin.AdminDto.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.dto.common.ListReqDto;
import rocket.planet.dto.project.ProjectDto.NameReqDto;
import rocket.planet.service.admin.AdminSearchService;
import rocket.planet.service.admin.AdminUserService;
import rocket.planet.service.auth.AuthorityService;
import rocket.planet.service.team.TeamService;

@RestController
@Slf4j
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AuthorityService authorityService;
	private final TeamService teamService;
	private final AdminSearchService adminSearchService;
	private final AdminUserService adminUserService;

	@PatchMapping("/user/auth")
	public ResponseEntity<String> authorityModify(@RequestBody AdminAuthModifyReqDto adminAuthModifyReqDto) {
		authorityService.modifyAuthority(adminAuthModifyReqDto);

		return ResponseEntity.ok().body(adminAuthModifyReqDto.getUserNickName() + "님의 권한이 수정되었습니다.");
	}

	@GetMapping("/user/auth")
	public ResponseEntity<AdminAuthMemberListDto> adminTeamMemberList(@ModelAttribute ListReqDto listReqDto,
		@RequestParam String teamName) {

		AdminAuthMemberListDto teamMemberList = authorityService.getTeamMemberList(listReqDto, teamName);

		return ResponseEntity.ok().body(teamMemberList);

	}

	@GetMapping("/user/auth/search")
	public ResponseEntity<AdminAuthMemberListDto> userAuthSearch(@ModelAttribute ListReqDto listReqDto,
		@RequestParam("userNickName") String userNickName) {
		AdminAuthMemberListDto member = adminSearchService.searchAuthUser(listReqDto, userNickName);
		return ResponseEntity.ok().body(member);
	}

	@GetMapping("/user/orgs/search")
	public ResponseEntity<AdminMemberOrgListDto> userOrgSearch(@ModelAttribute ListReqDto listReqDto,
		@RequestParam("userNickName") String userNickName) {
		AdminMemberOrgListDto memberList = adminSearchService.searchOrgUser(listReqDto, userNickName);
		return ResponseEntity.ok().body(memberList);
	}

	@PatchMapping("/user/orgs")
	public ResponseEntity<String> orgModify(@ModelAttribute List<AdminOrgModifyReqDto> orgModifyReqList) {
		teamService.modifyMemberOrg(orgModifyReqList);

		return ResponseEntity.ok().body("사용자(들)의 소속을 변경하였습니다.");
	}

	@PatchMapping("/user/disable")
	public ResponseEntity<String> userRemove(@RequestBody NameReqDto userNickName) {
		adminUserService.disabledUser(userNickName.getName());

		return ResponseEntity.ok().body(userNickName.getName() + "를 퇴사 처리하였습니다.");
	}
}
