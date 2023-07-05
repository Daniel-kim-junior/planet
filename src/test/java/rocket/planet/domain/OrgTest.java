package rocket.planet.domain;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import rocket.planet.repository.jpa.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

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
    private UserRepository userRepository;

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


        Optional<User> admin = userRepository.findByUserIdContaining("admin");
        Optional<User> crew = userRepository.findByUserIdContaining("crew");
        Optional<User> pilot = userRepository.findByUserIdContaining("pilot");
        Optional<User> captain = userRepository.findByUserIdContaining("captain");
        Optional<User> radar = userRepository.findByUserIdContaining("radar");
        Optional<User> pl = userRepository.findByUserIdContaining("pl");


        Org adminOrg = Org.builder()
                .user(admin.get())
                .company(company)
                .department(hrDept)
                .team(hrTeam)
                .belongStartDate(LocalDate.now())
                .belongInviter("어드민")
                .belongStatus(true)
                .build();
        orgRepository.save(adminOrg);
        Org crewOrg = Org.builder()
                .user(crew.get())
                .company(company)
                .department(smartDept)
                .team(smartCityTeam)
                .belongStartDate(LocalDate.now())
                .belongInviter("어드민")
                .belongStatus(true)
                .build();
        orgRepository.save(crewOrg);
        Org pilotOrg = Org.builder()
                .user(pilot.get())
                .company(company)
                .department(aiChatbotDept)
                .team(aiChatbotTeam)
                .belongStartDate(LocalDate.now())
                .belongInviter("어드민")
                .belongStatus(true)
                .build();
        orgRepository.save(pilotOrg);
        Org captainOrg = Org.builder()
                .user(captain.get())
                .company(company)
                .department(smartDept)
                .team(smartFactoryTeam)
                .belongStartDate(LocalDate.now())
                .belongInviter("어드민")
                .belongStatus(true)
                .build();
        orgRepository.save(captainOrg);
        Org radarOrg = Org.builder()
                .user(radar.get())
                .company(company)
                .department(internalSysDept)
                .team(internalSysTeam)
                .belongStartDate(LocalDate.now())
                .belongInviter("어드민")
                .belongStatus(true)
                .build();
        orgRepository.save(radarOrg);
        Org plOrg = Org.builder()
                .user(pl.get())
                .company(company)
                .department(itConsultingDept)
                .team(itConsultingTeam)
                .belongStartDate(LocalDate.now())
                .belongInviter("어드민")
                .belongStatus(true)
                .build();
        orgRepository.save(plOrg);



    }

}
