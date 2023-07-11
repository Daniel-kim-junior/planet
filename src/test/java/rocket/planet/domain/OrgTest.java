package rocket.planet.domain;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import rocket.planet.repository.jpa.CompanyRepository;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.OrgRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.TeamRepository;

@SpringBootTest
public class OrgTest {
	@Autowired
	private OrgRepository orgRepository;
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private DeptRepository deptRepository;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private ProfileRepository profileRepository;

	@Test
	@Rollback(false)
	void saveOrg() {
		Company company = companyRepository.findByCompanyName("dktechin");

		Department hrDept = deptRepository.findByDeptName("경영지원");
		Department smartDept = deptRepository.findByDeptName("스마트솔루션");
		Department aiChatbotDept = deptRepository.findByDeptName("AI챗봇");
		Department internalSysDept = deptRepository.findByDeptName("기업솔루션");
		Department itConsultingDept = deptRepository.findByDeptName("IT아웃소싱");

		Team hrTeam = teamRepository.findByTeamName("인사");
		Team smartFactoryTeam = teamRepository.findByTeamName("스마트팩토리");
		Team aiChatbotTeam = teamRepository.findByTeamName("AI챗봇구축");
		Team internalSysTeam = teamRepository.findByTeamName("사내정보시스템");
		Team smartCityTeam = teamRepository.findByTeamName("스마트시티");
		Team itConsultingTeam = teamRepository.findByTeamName("IT컨설팅");


		Profile admin = profileRepository.findByUserNickName("admin").get();
		Profile crew = profileRepository.findByUserNickName("crew").get();
		Profile pilot = profileRepository.findByUserNickName("pilot").get();
		Profile captain = profileRepository.findByUserNickName("captain").get();
		Profile radar = profileRepository.findByUserNickName("radar").get();
		Profile pl = profileRepository.findByUserNickName("plpl").get();

		System.out.println(admin);
		Org adminOrg = Org.builder()
			.profile(admin)
			.company(company)
			.department(hrDept)
			.team(hrTeam)
			.orgStartDate(LocalDate.now())
			.orgInviter(admin.getUserName())
			.orgStatus(true)
			.build();
		orgRepository.save(adminOrg);
		Org crewOrg = Org.builder()
			.profile(crew)
			.company(company)
			.department(smartDept)
			.team(smartCityTeam)
			.orgStartDate(LocalDate.now())
			.orgInviter(admin.getUserName())
			.orgStatus(true)
			.build();
		orgRepository.save(crewOrg);
		Org pilotOrg = Org.builder()
			.profile(pilot)
			.company(company)
			.department(aiChatbotDept)
			.team(aiChatbotTeam)
			.orgStartDate(LocalDate.now())
			.orgInviter(admin.getUserName())
			.orgStatus(true)
			.build();
		orgRepository.save(pilotOrg);
		Org captainOrg = Org.builder()
			.profile(captain)
			.company(company)
			.department(smartDept)
			.team(smartFactoryTeam)
			.orgStartDate(LocalDate.now())
			.orgInviter(admin.getUserName())
			.orgStatus(true)
			.build();
		orgRepository.save(captainOrg);
		Org radarOrg = Org.builder()
			.profile(radar)
			.company(company)
			.department(internalSysDept)
			.team(internalSysTeam)
			.orgStartDate(LocalDate.now())
			.orgInviter(admin.getUserName())
			.orgStatus(true)
			.build();
		orgRepository.save(radarOrg);
		Org plOrg = Org.builder()
			.profile(pl)
			.company(company)
			.department(itConsultingDept)
			.team(itConsultingTeam)
			.orgStartDate(LocalDate.now())
			.orgInviter(admin.getUserName())
			.orgStatus(true)
			.build();
		orgRepository.save(plOrg);


	}

}
