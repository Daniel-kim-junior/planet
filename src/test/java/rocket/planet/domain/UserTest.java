package rocket.planet.domain;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import rocket.planet.repository.jpa.AuthRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.UserRepository;

@SpringBootTest
public class UserTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Rollback(false)
    void saveUser() {

        Profile adminProfile = Profile.builder()
                .profileBirthDt(LocalDate.of(1995, 3, 22))
                .role(Role.ADMIN)
                .userId("admin@gmail.com")
                .userName("어드민")
                .profileDisplay(true)
                .profileCareer(1)
                .profileAnnualStatus(true)
                .build();

        User adminUser = User.builder()
                .userPwd(passwordEncoder.encode("admin111!"))
                .userLock(false)
                .profile(adminProfile)
                .userAccessDt(LocalDate.now())
                .userId(adminProfile.getUserId())
                .build();
        profileRepository.save(adminProfile);
        userRepository.save(adminUser);

        Profile crewProfile = Profile.builder()
                .profileBirthDt(LocalDate.of(1996, 8, 22))
                .role(Role.CREW)
                .userId("crew@gmail.com")
                .userName("김크루")
                .profileDisplay(true)
                .profileCareer(5)
                .profileAnnualStatus(true)
                .build();
        User crewUser = User.builder()
                .userPwd("crew111!")
                .userLock(false)
                .userAccessDt(LocalDate.now())
                .userId(crewProfile.getUserId())
                .build();
        profileRepository.saveAndFlush(crewProfile);
        userRepository.saveAndFlush(crewUser);

        Profile pilotProfile = Profile.builder()
                .profileBirthDt(LocalDate.of(1994, 1, 17))
                .role(Role.PILOT)
                .userId("pilot@gmail.com")
                .userName("파일럿")
                .profileDisplay(true)
                .profileCareer(10)
                .profileAnnualStatus(true)
                .build();
        User pilotUser = User.builder()
                .userPwd("pilot111!")
                .userLock(false)
                .userAccessDt(LocalDate.now())
                .userId(pilotProfile.getUserId())
                .build();
        profileRepository.saveAndFlush(pilotProfile);
        userRepository.saveAndFlush(pilotUser);

        Profile captainProfile = Profile.builder()
                .profileBirthDt(LocalDate.of(1999, 8, 23))
                .role(Role.CAPTAIN)
                .userId("captain@gmail.com")
                .userName("강캡틴")
                .profileDisplay(true)
                .profileCareer(15)
                .profileAnnualStatus(false)
                .build();
        User captainUser = User.builder()
                .userPwd("captain111!")
                .userLock(false)
                .userAccessDt(LocalDate.now())
                .userId(captainProfile.getUserId())
                .build();
        profileRepository.saveAndFlush(captainProfile);
        userRepository.saveAndFlush(captainUser);

        Profile radarProfile = Profile.builder()
                .profileBirthDt(LocalDate.of(1997, 12, 20))
                .role(Role.RADAR)
                .userId("radar@gmail.com")
                .userName("레이더")
                .profileDisplay(true)
                .profileCareer(7)
                .profileAnnualStatus(true)
                .build();
        User radarUser = User.builder()
                .userPwd("radar111!")
                .userLock(false)
                .userAccessDt(LocalDate.now())
                .userId(radarProfile.getUserId())
                .build();
        profileRepository.saveAndFlush(radarProfile);
        userRepository.saveAndFlush(radarUser);

        Profile plProfile = Profile.builder()
                .profileBirthDt(LocalDate.of(1994, 7, 30))
                .role(Role.CREW)
                .userId("plpl@gmail.com")
                .userName("홍피엘")
                .profileDisplay(true)
                .profileCareer(3)
                .profileAnnualStatus(false)
                .build();
        User plUser = User.builder()
                .userPwd("plpl111!")
                .userLock(false)
                .userAccessDt(LocalDate.now())
                .userId(plProfile.getUserId())
                .build();
        profileRepository.saveAndFlush(plProfile);
        userRepository.saveAndFlush(plUser);


    }

}
