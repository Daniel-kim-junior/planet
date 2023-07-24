package rocket.planet.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.TeamRepository;

@SpringBootTest
@Transactional
public class TeamTest {

	@Autowired
	private DeptRepository deptRepository;
	@Autowired
	private TeamRepository teamRepository;

	@Test
	@Rollback(false)
	public void createTeamTestData() {
		Department smartSolutionDept = deptRepository.findByDeptName("스마트솔루션");
		Department hrDept = deptRepository.findByDeptName("경영지원");
		Department aiChatbotDept = deptRepository.findByDeptName("AI챗봇");
		Department ftDept = deptRepository.findByDeptName("기업솔루션");
		Department itOutDept = deptRepository.findByDeptName("IT아웃소싱");
		Department noneDept = deptRepository.findByDeptName("부문없음");

		Team smartFactoryTeam = Team.builder()
			.department(smartSolutionDept)
			.teamName("스마트팩토리")
			.teamInactive(false)
			.teamDesc("스마트팩토리 팀입니다.")
			.teamType(OrgType.DEVELOPMENT)
			.build();
		teamRepository.saveAndFlush(smartFactoryTeam);

		Team smartCityTeam = Team.builder()
			.department(smartSolutionDept)
			.teamName("스마트시티")
			.teamInactive(false)

			.teamDesc("스마트시티 팀입니다.")
			.teamType(OrgType.DEVELOPMENT)
			.build();
		teamRepository.saveAndFlush(smartCityTeam);

		Team hrTeam = Team.builder()
			.department(hrDept)
			.teamName("인사")
			.teamInactive(false)

			.teamDesc("인사 팀입니다.")
			.teamType(OrgType.NON_DEVELOPMENT)
			.build();
		teamRepository.saveAndFlush(hrTeam);

		Team adminTeam = Team.builder()
			.department(hrDept)
			.teamName("총무")
			.teamInactive(false)

			.teamDesc("총무 팀입니다.")
			.teamType(OrgType.NON_DEVELOPMENT)
			.build();
		teamRepository.saveAndFlush(adminTeam);

		Team aiChatbotTeam = Team.builder()
			.department(aiChatbotDept)
			.teamName("AI챗봇구축")
			.teamInactive(false)

			.teamDesc("AI챗봇구축 팀입니다.")
			.teamType(OrgType.DEVELOPMENT)
			.build();
		teamRepository.saveAndFlush(aiChatbotTeam);

		Team internalSysTeam = Team.builder()
			.department(ftDept)
			.teamName("사내정보시스템")
			.teamInactive(false)

			.teamDesc("사내정보시스템 팀입니다.")
			.teamType(OrgType.DEVELOPMENT)
			.build();
		teamRepository.saveAndFlush(internalSysTeam);

		Team itConsultingTeam = Team.builder()
			.department(itOutDept)
			.teamName("IT컨설팅")
			.teamInactive(false)
			.teamDesc("IT컨설팅 팀입니다.")
			.teamType(OrgType.DEVELOPMENT)
			.build();
		teamRepository.saveAndFlush(itConsultingTeam);

	}

}
