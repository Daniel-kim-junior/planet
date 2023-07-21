package rocket.planet.controller.admin;

import static rocket.planet.dto.admin.AdminDto.*;

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
import rocket.planet.dto.common.CommonResDto;
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
	public ResponseEntity<CommonResDto> authorityModify(@RequestBody AdminAuthModifyReqDto adminAuthModifyReqDto) {

		return ResponseEntity.ok().body(authorityService.modifyAuthority(adminAuthModifyReqDto));
	}

	@GetMapping("/user/auth")
	public ResponseEntity<AdminAuthMemberListDto> adminTeamMemberList(@ModelAttribute ListReqDto listReqDto,
		@RequestParam String teamName) {

		return ResponseEntity.ok().body(authorityService.getTeamMemberList(listReqDto, teamName));

	}

	@GetMapping("/user/auth/search")
	public ResponseEntity<AdminAuthMemberListDto> userAuthSearch(@ModelAttribute ListReqDto listReqDto,
		String userNickName) {

		return ResponseEntity.ok().body(adminSearchService.searchAuthUser(listReqDto, userNickName));
	}

	@GetMapping("/user/orgs/search")
	public ResponseEntity<AdminMemberOrgListDto> userOrgSearch(@ModelAttribute ListReqDto listReqDto,
		String userNickName) {

		return ResponseEntity.ok().body(adminSearchService.searchOrgUser(listReqDto, userNickName));
	}

	@PatchMapping("/user/orgs")
	public ResponseEntity<CommonResDto> orgModify(@RequestBody AdminOrgModifyReqDto orgModifyReqList) {

		return ResponseEntity.ok().body(teamService.modifyMemberOrg(orgModifyReqList));
	}

	@PatchMapping("/user/disable")
	public ResponseEntity<CommonResDto> userRemove(@RequestBody NameReqDto userNickName) {

		return ResponseEntity.ok().body(adminUserService.disabledUser(userNickName.getName()));
	}
}
