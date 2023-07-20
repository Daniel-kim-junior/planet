package rocket.planet.controller.admin;

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
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.service.admin.AdminUserService;
import rocket.planet.service.auth.AuthorityService;
import rocket.planet.service.team.TeamService;

@SpringBootTest
class AdminControllerTest {

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private TeamService teamService;

	@Autowired
	private AdminUserService adminUserService;

	@Autowired
	private AuthRepository authRepository;

	@Autowired
	private PfAuthRepository pfAuthRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	@Transactional
	void 권한_변경_테스트() {

		AdminAuthModifyReqDto adminAuthModifyReqDto = AdminAuthModifyReqDto.builder()
			.userNickName("pilot")
			.deptName("AI챗봇")
			.teamName("AI챗봇구축")
			.role("PILOT")
			.build();

		authorityService.modifyAuthority(adminAuthModifyReqDto);
		Profile profile = profileRepository.findByUserNickName("pilot").orElseThrow();
		System.out.println(profile.getRole());

	}

	@Test
	void 관리자_팀원_리스트_조회_테스트() {

		String teamName = "인사";
		ListReqDto listReqDto = ListReqDto.builder().page(1).pageSize(8).build();
		AdminAuthMemberListDto teamMemberList = authorityService.getTeamMemberList(listReqDto, teamName);

		System.out.println(teamMemberList.getAdminAuthMemberList());
	}

	@Transactional
	@Test
	void 관리자_직원_소속_변경_테스트() {
		AdminOrgModifyReqDto orgModify1 = AdminOrgModifyReqDto.builder()
			.userNickName("crew")
			.deptName("AI챗봇")
			.teamName("AI챗봇구축")
			.build();
		// AdminOrgModifyReqDto orgModify2 = AdminOrgModifyReqDto.builder().userNickName("pilot").deptName("스마트솔루션").teamName("스마트팩토리").build()

		teamService.modifyMemberOrg(orgModify1);

	}

	@Test
	@Transactional
	void 퇴사자_처리_테스트() {
		adminUserService.disabledUser("captain");
		//
		// assertThat(authRepository.count()).isEqualTo(0);
		// assertThat(pfAuthRepository.count()).isEqualTo(0);

		System.out.println("profile=======> " + profileRepository.findByUserNickName("plpl").get());
		System.out.println(
			"user==========>" + userRepository.findByProfile(profileRepository.findByUserNickName("plpl").get()));

	}
}