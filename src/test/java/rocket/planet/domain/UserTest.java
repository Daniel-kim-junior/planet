package rocket.planet.domain;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import rocket.planet.repository.jpa.AuthRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.UserRepository;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
public class UserTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private ProfileRepository profileRepository;

    @Test
    @Rollback(false)
    void saveUser() {
        UUID adminAuthorityId = UUID.randomUUID();

        Authority adminAuthority = Authority.builder()
                .authType(AuthType.ADMIN)
                .authTargetId(adminAuthorityId)
                .authorizerId("admin@gmail.com")
                .build();
        authRepository.saveAndFlush(adminAuthority);

        User adminUser = User.builder()
                .userPwd("admin111!")
                .role(Role.ADMIN)
                .userLock(false)
                .userAccessDt(LocalDate.now())
                .userName("어드민")
                .userId("admin@gmail.com")
                .authority(adminAuthority)
                .build();

        Profile adminProfile = Profile.builder()
                .profileBirthDt(LocalDate.of(1995, 3, 22))
                .profileDisplay(true)
                .profileCareer(1)
                .profileAnnualStatus(true)
                .build();

        profileRepository.saveAndFlush(adminProfile);
        adminUser.updateProfile(adminProfile);
        userRepository.saveAndFlush(adminUser);


        Authority crewAuthority = Authority.builder()
                .authType(AuthType.CREW)
                .authTargetId(adminAuthority.getId())
                .authorizerId("crew@gmail.com")
                .build();
        authRepository.saveAndFlush(crewAuthority);
        User crewUser = User.builder()

                .userPwd("crew111!")
                .role(Role.CREW)
                .userLock(false)
                .userAccessDt(LocalDate.now())
                .userName("김크루")
                .userId("crew@gmail.com")
                .authority(crewAuthority)
                .build();
        Profile crewProfile = Profile.builder()

                .profileBirthDt(LocalDate.of(1996, 8, 22))
                .profileDisplay(true)
                .profileCareer(5)
                .profileAnnualStatus(true)
                .build();
        profileRepository.saveAndFlush(crewProfile);
        crewUser.updateProfile(crewProfile);
        userRepository.saveAndFlush(crewUser);

        Authority pilotAuthority = Authority.builder()
                .authType(AuthType.TEAM)
                .authTargetId(adminAuthority.getId())
                .authorizerId("pilot@gmail.com")
                .build();
        authRepository.saveAndFlush(pilotAuthority);
        User pilotUser = User.builder()
                .userPwd("pilot111!")
                .role(Role.PILOT)
                .userLock(false)
                .userAccessDt(LocalDate.now())
                .userName("파일럿")
                .userId("pilot@gmail.com")
                .authority(pilotAuthority)
                .build();
        Profile pilotProfile = Profile.builder()
                .profileBirthDt(LocalDate.of(1994, 1, 17))
                .profileDisplay(true)
                .profileCareer(10)
                .profileAnnualStatus(true)
                .build();
        profileRepository.saveAndFlush(pilotProfile);
        pilotUser.updateProfile(pilotProfile);
        userRepository.saveAndFlush(pilotUser);

        Authority captainAuthority = Authority.builder()

                .authType(AuthType.DEPARTMENT)
                .authTargetId(adminAuthority.getId())
                .authorizerId("captain@gmail.com")
                .build();
        authRepository.saveAndFlush(captainAuthority);
        User captainUser = User.builder()

                .userPwd("captain111!")
                .role(Role.CAPTAIN)
                .userLock(false)
                .userAccessDt(LocalDate.now())
                .userName("강캡틴")
                .userId("captain@gmail.com")
                .authority(captainAuthority)
                .build();
        Profile captainProfile = Profile.builder()

                .profileBirthDt(LocalDate.of(1999, 8, 23))
                .profileDisplay(true)
                .profileCareer(15)
                .profileAnnualStatus(false)
                .build();

        profileRepository.saveAndFlush(captainProfile);
        captainUser.updateProfile(captainProfile);
        userRepository.saveAndFlush(captainUser);

        Authority radarAuthority = Authority.builder()

                .authType(AuthType.COMPANY)
                .authTargetId(adminAuthority.getId())
                .authorizerId("radar@gmail.com")
                .build();
        authRepository.saveAndFlush(radarAuthority);
        User radarUser = User.builder()

                .userPwd("radar111!")
                .role(Role.RADAR)
                .userLock(false)
                .userAccessDt(LocalDate.now())
                .userName("레이더")
                .userId("radar@gmail.com")
                .authority(radarAuthority)
                .build();
        Profile radarProfile = Profile.builder()

                .profileBirthDt(LocalDate.of(1997, 12, 20))
                .profileDisplay(true)
                .profileCareer(7)
                .profileAnnualStatus(true)
                .build();

        profileRepository.saveAndFlush(radarProfile);
        radarUser.updateProfile(radarProfile);
        userRepository.saveAndFlush(radarUser);


        Authority plAuthority = Authority.builder()

                .authType(AuthType.PROJECT)
                .authTargetId(adminAuthority.getId())
                .authorizerId("plpl@gmail.com")
                .build();
        authRepository.saveAndFlush(plAuthority);
        User plUser = User.builder()

                .userPwd("plpl111!")
                .role(Role.CREW)
                .userLock(false)
                .userAccessDt(LocalDate.now())
                .userName("박피엘")
                .userId("plpl@gmail.com")
                .authority(plAuthority)
                .build();
        Profile plProfile = Profile.builder()

                .profileBirthDt(LocalDate.of(1998, 12, 5))
                .profileDisplay(true)
                .profileCareer(3)
                .profileAnnualStatus(false)
                .build();

        profileRepository.saveAndFlush(plProfile);
        plUser.updateProfile(plProfile);
        userRepository.saveAndFlush(plUser);


    }

}
