package rocket.planet.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.domain.Profile;
import rocket.planet.dto.admin.AdminDto.AdminAuthMemberListDto;
import rocket.planet.dto.admin.AdminDto.AdminAuthModifyReqDto;
import rocket.planet.dto.admin.AdminDto.AdminOrgModifyReqDto;
import rocket.planet.dto.common.ListReqDto;
import rocket.planet.repository.jpa.AuthRepository;
import rocket.planet.repository.jpa.PfAuthRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.ProjectRepository;
import rocket.planet.repository.jpa.UserPjtRepository;
import rocket.planet.service.auth.AuthorityService;
import rocket.planet.service.project.ProjectService;
import rocket.planet.service.team.TeamService;

@SpringBootTest
class AdminControllerTest {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private UserPjtRepository userPjtRepository;

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private AuthRepository authRepository;

	@Autowired
	private PfAuthRepository pfAuthRepository;

	@Autowired
	private TeamService teamService;

	@Test
	@Transactional
	void 권한_변경_테스트() {

		AdminAuthModifyReqDto adminAuthModifyReqDto = AdminAuthModifyReqDto.builder()
			.userNickName("crew")
			.deptName("AI챗봇")
			.teamName("AI챗봇구축")
			.role("PILOT")
			.build();

		authorityService.modifyAuthority(adminAuthModifyReqDto);
		Profile profile = profileRepository.findByUserNickName("crew").orElseThrow();
		System.out.println(profile.getRole());

	}

	@Test
	void 관리자_팀원_리스트_조회_테스트() {

		String teamName = "AI챗봇구축";
		ListReqDto listReqDto = ListReqDto.builder().page(1).pageSize(8).build();
		AdminAuthMemberListDto teamMemberList = authorityService.getTeamMemberList(listReqDto, teamName);

		System.out.println(teamMemberList.toString());
	}

	@Transactional
	@Test
	void 관리자_직원_소속_변경_테스트() {
		List<AdminOrgModifyReqDto> orgModifyReqList = new ArrayList<>();
		orgModifyReqList.add(
			AdminOrgModifyReqDto.builder().userNickName("crew").deptName("AI챗봇").teamName("AI챗봇구축").build());
		orgModifyReqList.add(
			AdminOrgModifyReqDto.builder().userNickName("pilot").deptName("스마트솔루션").teamName("스마트팩토리").build());

		teamService.modifyMemberOrg(orgModifyReqList);

	}
}