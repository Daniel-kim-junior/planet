package rocket.planet.domain;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import rocket.planet.repository.jpa.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
public class TestData {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OrgRepository orgRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private DeptRepository deptRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private TechRepository techRepository;
    @Autowired
    private PfTechRepository pfTechRepository;


    @Test
    @Rollback(value = false)
    @Order(1)
    void 사원_120명_생성() {
        Company company = companyRepository.findByCompanyName("dktechin");

        Department hrDept = deptRepository.findByDeptName("경영지원");
        Department smartDept = deptRepository.findByDeptName("스마트솔루션");
        Department aiChatbotDept = deptRepository.findByDeptName("AI챗봇");
        Department internalSysDept = deptRepository.findByDeptName("기업솔루션");
        Department itConsultingDept = deptRepository.findByDeptName("IT아웃소싱");

        Team hrTeam = teamRepository.findByTeamName("인사");
        Team generalAffairsTeam = teamRepository.findByTeamName("총무");
        Team smartFactoryTeam = teamRepository.findByTeamName("스마트팩토리");
        Team aiChatbotTeam = teamRepository.findByTeamName("AI챗봇구축");
        Team internalSysTeam = teamRepository.findByTeamName("사내정보시스템");
        Team smartCityTeam = teamRepository.findByTeamName("스마트시티");
        Team itConsultingTeam = teamRepository.findByTeamName("IT컨설팅");

        List<Department> departments = Arrays.asList(smartDept, aiChatbotDept, internalSysDept, itConsultingDept);
        Map<Department, List<Team>> teamsByDepartment = new HashMap<>();
        teamsByDepartment.put(smartDept, Arrays.asList(smartCityTeam, smartFactoryTeam));
        teamsByDepartment.put(aiChatbotDept, Arrays.asList(aiChatbotTeam));
        teamsByDepartment.put(internalSysDept, Arrays.asList(internalSysTeam));
        teamsByDepartment.put(itConsultingDept, Arrays.asList(itConsultingTeam));

        List<Department> noneDevDepartments = Arrays.asList(hrDept);
        Map<Department, List<Team>> teamsByNoneDevDepartment = new HashMap<>();
        teamsByNoneDevDepartment.put(hrDept, Arrays.asList(hrTeam, generalAffairsTeam));

        Profile admin = profileRepository.findByUserNickName("admin").get();

        for (int i = 0; i < 100; i++) {
            String email = "engineer" + i + "@gmail.com";
            String password = "password" + i + "!";
            String nickname = "engineer" + i;

            if (nickname.startsWith("engineer")) {
                User user = User.builder()
                        .userPwd(passwordEncoder.encode(password))
                        .userLock(false)
                        .lastPwdModifiedDt(LocalDate.now())
                        .userId(email)
                        .build();
                User savedUser = userRepository.save(user);

                LocalDate startDate = LocalDate.of(2015, 8, 20);
                LocalDate endDate = LocalDate.of(2023, 7, 22);

                int randomYear = ThreadLocalRandom.current().nextInt(startDate.getYear(), endDate.getYear() + 1);
                int randomMonth = ThreadLocalRandom.current().nextInt(1, 13);
                int randomDay = ThreadLocalRandom.current().nextInt(1, YearMonth.of(randomYear, randomMonth).lengthOfMonth() + 1);

                LocalDate randomStartDate = LocalDate.of(randomYear, randomMonth, randomDay);
                int randomCareer = ThreadLocalRandom.current().nextInt(1, 16);
                Profile profile = Profile.builder()
                        .profileBirthDt(LocalDate.of(1995, 3, 15))
                        .role(Role.CREW)
                        .userId(email)
                        .profileStartDate(randomStartDate)
                        .userNickName(nickname)
                        .userName("개발자" + i)
                        .profileStatus(true)
                        .profileDisplay(true)
                        .profileCareer(randomCareer)
                        .profileAnnualStatus(false)
                        .build();
                profileRepository.save(profile);
                savedUser.updateProfile(profile);

                int randomDeptIndex = ThreadLocalRandom.current().nextInt(departments.size());
                Department randomDept = departments.get(randomDeptIndex);
                List<Team> teamsInRandomDept = teamsByDepartment.get(randomDept);
                int randomTeamIndex = ThreadLocalRandom.current().nextInt(teamsInRandomDept.size());
                Team randomTeam = teamsInRandomDept.get(randomTeamIndex);

                Org userOrg = Org.builder()
                        .profile(profile)
                        .company(company)
                        .department(randomDept)
                        .team(randomTeam)
                        .orgInviter(admin.getUserName())
                        .orgStatus(true)
                        .build();
                orgRepository.save(userOrg);

            }

        }

        for (int i = 0; i < 20; i++) {
            String email = "staff" + i + "@gmail.com";
            String password = "password" + i + "!";
            String nickname = "staff" + i;

            if (nickname.startsWith("staff")) {
                User user = User.builder()
                        .userPwd(passwordEncoder.encode(password))
                        .userLock(false)
                        .lastPwdModifiedDt(LocalDate.now())
                        .userId(email)
                        .build();
                User savedUser = userRepository.save(user);

                LocalDate startDate = LocalDate.of(2015, 8, 20);
                LocalDate endDate = LocalDate.of(2023, 7, 22);
                LocalDate randomStartDate = startDate.plusDays(ThreadLocalRandom.current().nextLong(startDate.until(endDate).getDays()));
                int randomCareer = ThreadLocalRandom.current().nextInt(1, 16);
                Profile profile = Profile.builder()
                        .profileBirthDt(LocalDate.of(1995, 3, 15))
                        .role(Role.CREW)
                        .userId(email)
                        .profileStartDate(randomStartDate)
                        .userNickName(nickname)
                        .userName("경영지원" + i)
                        .profileStatus(true)
                        .profileDisplay(true)
                        .profileCareer(randomCareer)
                        .profileAnnualStatus(false)
                        .build();
                profileRepository.save(profile);
                savedUser.updateProfile(profile);

                int randomDeptIndex = ThreadLocalRandom.current().nextInt(noneDevDepartments.size());
                Department randomDept = noneDevDepartments.get(randomDeptIndex);
                List<Team> teamsInRandomDept = teamsByNoneDevDepartment.get(randomDept);
                int randomTeamIndex = ThreadLocalRandom.current().nextInt(teamsInRandomDept.size());
                Team randomTeam = teamsInRandomDept.get(randomTeamIndex);

                Org userOrg = Org.builder()
                        .profile(profile)
                        .company(company)
                        .department(randomDept)
                        .team(randomTeam)
                        .orgInviter(admin.getUserName())
                        .orgStatus(true)
                        .build();
                orgRepository.save(userOrg);
            }
        }


    }

    @Test
    @Order(2)
    @Rollback(value = false)
    void 기술_랜덤하게_추가() {
        Tech lang1 = techRepository.findByTechName("Java");
        Tech lang2 = techRepository.findByTechName("Python");
        Tech lang3 = techRepository.findByTechName("Kotlin");
        Tech lang4 = techRepository.findByTechName("C");
        Tech lang5 = techRepository.findByTechName("PHP");
        Tech lang6 = techRepository.findByTechName("Ruby");
        Tech lang7 = techRepository.findByTechName("C++");
        Tech lang8 = techRepository.findByTechName("JavaScript");
        Tech frame1 = techRepository.findByTechName("Spring");
        Tech frame2 = techRepository.findByTechName("SpringBoot");
        Tech frame3 = techRepository.findByTechName("Django");
        Tech frame4 = techRepository.findByTechName("Flutter");
        Tech frame5 = techRepository.findByTechName("Vue.js");
        Tech frame6 = techRepository.findByTechName("React.js");
        Tech db1 = techRepository.findByTechName("MySQL");
        Tech db2 = techRepository.findByTechName("Oracle");
        Tech db3 = techRepository.findByTechName("Redis");

        List<Tech> techList = Arrays.asList(lang1, lang2, lang3, lang4, lang5, lang6, lang7, lang8,
                frame1, frame2, frame3, frame4, frame5, frame6,
                db1, db2, db3);

        List<Profile> engineerUsers = profileRepository.findByUserNickNameStartsWith("engineer");
        for (Profile profile : engineerUsers) {
            List<Tech> randomTechList = getRandomTechList(techList, 5); // 랜덤한 5개의 기술 선택

            for (Tech tech : randomTechList) {
                ProfileTech profileTech = ProfileTech.builder()
                        .tech(tech)
                        .profile(profile)
                        .build();
                pfTechRepository.save(profileTech);
            }
        }
    }

    List<Tech> getRandomTechList(List<Tech> techList, int count) {
        List<Tech> randomTechList = new ArrayList<>();
        List<Tech> copyTechList = new ArrayList<>(techList);

        int techListSize = techList.size();
        for (int i = 0; i < count && i < techListSize; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(copyTechList.size());
            Tech tech = copyTechList.get(randomIndex);
            randomTechList.add(tech);
            copyTechList.remove(randomIndex);
        }

        return randomTechList;
    }

}
