package rocket.planet.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import rocket.planet.repository.jpa.CompanyRepository;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.TeamRepository;

import java.util.UUID;

@SpringBootTest
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

        Team smartFactoryTeam = Team.builder()
                .department(smartSolutionDept)
                .teamName("스마트팩토리")
                .teamDesc("스마트팩토리 팀입니다.")
                .teamType(OrgType.DEVELOPMENT)
                .build();
        teamRepository.saveAndFlush(smartFactoryTeam);

        Team smartCityTeam = Team.builder()
                .department(smartSolutionDept)
                .teamName("스마트시티")
                .teamDesc("스마트시티 팀입니다.")
                .teamType(OrgType.DEVELOPMENT)
                .build();
        teamRepository.saveAndFlush(smartCityTeam);

        Team hrTeam = Team.builder()
                .department(hrDept)
                .teamName("인사")
                .teamDesc("인사 팀입니다.")
                .teamType(OrgType.NON_DEVELOPMENT)
                .build();
        teamRepository.saveAndFlush(hrTeam);

        Team adminTeam = Team.builder()
                .department(hrDept)
                .teamName("총무")
                .teamDesc("총무 팀입니다.")
                .teamType(OrgType.NON_DEVELOPMENT)
                .build();
        teamRepository.saveAndFlush(adminTeam);

        Team aiChatbotTeam = Team.builder()
                .department(aiChatbotDept)
                .teamName("AI챗봇구축")
                .teamDesc("AI챗봇구축 팀입니다.")
                .teamType(OrgType.DEVELOPMENT)
                .build();
        teamRepository.saveAndFlush(aiChatbotTeam);

        Team internalSysTeam = Team.builder()
                .department(ftDept)
                .teamName("사내정보시스템")
                .teamDesc("사내정보시스템 팀입니다.")
                .teamType(OrgType.DEVELOPMENT)
                .build();
        teamRepository.saveAndFlush(internalSysTeam);

        Team itConsultingTeam = Team.builder()
                .department(ftDept)
                .teamName("IT컨설팅")
                .teamDesc("IT컨설팅 팀입니다.")
                .teamType(OrgType.DEVELOPMENT)
                .build();
        teamRepository.saveAndFlush(itConsultingTeam);
    }

}
