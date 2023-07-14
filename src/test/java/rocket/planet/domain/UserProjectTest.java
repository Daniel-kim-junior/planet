package rocket.planet.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import rocket.planet.repository.jpa.*;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
public class UserProjectTest {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserPjtRepository userPjtRepository;

    @Test
    @Rollback(false)
    void saveUserProject() {

        Project pj1 = projectRepository.findByProjectName("스마트 시티 TF").get();

        Profile crew = profileRepository.findByUserNickName("crew").get();
        Profile pilot = profileRepository.findByUserNickName("pilot").get();
        Profile captain = profileRepository.findByUserNickName("captain").get();

        UserProject crewProject = UserProject.builder()
                .profile(crew)
                .project(pj1)
                .userPjtInviter("captain")
                .userPjtJoinDt(LocalDate.of(2023,7,5))
                .userPjtModifyDt(null)
                .userPjtCloseDt(LocalDate.of(2023,8,5))
                .userPjtCloseApply(false)
                .userPjtDesc("스마트 시티 건설 일조")
                .build();
        userPjtRepository.save(crewProject);

        UserProject pilotProject = UserProject.builder()
                .profile(pilot)
                .project(pj1)
                .userPjtInviter("captain")
                .userPjtJoinDt(LocalDate.of(2023,7,5))
                .userPjtModifyDt(LocalDate.of(2023,7,10))
                .userPjtCloseDt(LocalDate.of(2023,7,10))
                .userPjtCloseApply(true)
                .userPjtDesc("스마트한 세상 만들기")
                .build();
        userPjtRepository.save(pilotProject);
        UserProject captainProject = UserProject.builder()
                .profile(captain)
                .project(pj1)
                .userPjtInviter("captain")
                .userPjtJoinDt(LocalDate.of(2023,7,5))
                .userPjtModifyDt(null)
                .userPjtCloseDt(LocalDate.of(2023,8,14))
                .userPjtCloseApply(false)
                .userPjtDesc("스마트한 세상 만들기 개발")
                .build();
        userPjtRepository.save(captainProject);


    }

}
