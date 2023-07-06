package rocket.planet.domain;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import rocket.planet.repository.jpa.AuthRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.UserRepository;

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
			.authorizerId("rocket@gmail.com")
			.build();
		authRepository.saveAndFlush(adminAuthority);

		User adminUser = User.builder()
			.userPwd("admin111!")
			.userLock(false)
			.userAccessDt(LocalDate.now())
			.userId("admin@gmail.com")
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

		User crewUser = User.builder()
			.userPwd("crew111!")
			.userLock(false)
			.userAccessDt(LocalDate.now())
			.userId("crew@gmail.com")
			.build();
		Profile crewProfile = Profile.builder()
			.profileBirthDt(LocalDate.of(1996, 8, 22))
			.profileDisplay(true)
			.profileCareer(5)
			.profileAnnualStatus(true)
			.build();
		profileRepository.saveAndFlush(crewProfile);
		crewUser.updateProfile(crewProfile);
		User userCrew = userRepository.saveAndFlush(crewUser);

		UUID crew = userCrew.getId();

		Authority crewAuthority = Authority.builder()
			.authType(AuthType.CREW)
			.authTargetId(crew)
			.authorizerId("admin@gmail.com")
			.build();
		authRepository.saveAndFlush(crewAuthority);
		userRepository.saveAndFlush(crewUser);

		User pilotUser = User.builder()
			.userPwd("pilot111!")
			.userLock(false)
			.userAccessDt(LocalDate.now())
			.userId("pilot@gmail.com")
			.build();
		Profile pilotProfile = Profile.builder()
			.profileBirthDt(LocalDate.of(1994, 1, 17))
			.profileDisplay(true)
			.profileCareer(10)
			.profileAnnualStatus(true)
			.build();
		profileRepository.saveAndFlush(pilotProfile);
		pilotUser.updateProfile(pilotProfile);
		User userPilot = userRepository.saveAndFlush(pilotUser);

		UUID pilot = userPilot.getId();
		Authority pilotAuthority = Authority.builder()
			.authType(AuthType.TEAM)
			.authTargetId(pilot)
			.authorizerId("admin@gmail.com")
			.build();
		authRepository.saveAndFlush(pilotAuthority);
		userRepository.saveAndFlush(userPilot);

		User captainUser = User.builder()
			.userPwd("captain111!")
			.userLock(false)
			.userAccessDt(LocalDate.now())
			.userId("captain@gmail.com")
			.build();
		Profile captainProfile = Profile.builder()
			.profileBirthDt(LocalDate.of(1999, 8, 23))
			.profileDisplay(true)
			.profileCareer(15)
			.profileAnnualStatus(false)
			.build();

		profileRepository.saveAndFlush(captainProfile);
		captainUser.updateProfile(captainProfile);
		User userCaptain = userRepository.saveAndFlush(captainUser);

		UUID captain = userCaptain.getId();

		Authority captainAuthority = Authority.builder()
			.authType(AuthType.TEAM)
			.authTargetId(captain)
			.authorizerId("admin@gmail.com")
			.build();
		authRepository.saveAndFlush(captainAuthority);
		userRepository.saveAndFlush(captainUser);

		User radarUser = User.builder()
			.userPwd("radar111!")
			.userLock(false)
			.userAccessDt(LocalDate.now())
			.userId("radar@gmail.com")
			.build();
		Profile radarProfile = Profile.builder()
			.profileBirthDt(LocalDate.of(1997, 12, 20))
			.profileDisplay(true)
			.profileCareer(7)
			.profileAnnualStatus(true)
			.build();

		profileRepository.saveAndFlush(radarProfile);
		radarUser.updateProfile(radarProfile);
		User userRadar = userRepository.saveAndFlush(radarUser);

		UUID radar = userRadar.getId();
		Authority radarAuthority = Authority.builder()
			.authType(AuthType.TEAM)
			.authTargetId(radar)
			.authorizerId("admin@gmail.com")
			.build();
		authRepository.saveAndFlush(radarAuthority);
		userRepository.saveAndFlush(userRadar);

		User plUser = User.builder()
			.userPwd("plpl111!")
			.userLock(false)
			.userAccessDt(LocalDate.now())
			.userId("plpl@gmail.com")
			.build();
		Profile plProfile = Profile.builder()
			.profileBirthDt(LocalDate.of(1998, 12, 5))
			.profileDisplay(true)
			.profileCareer(3)
			.profileAnnualStatus(false)
			.build();
		profileRepository.saveAndFlush(plProfile);
		plUser.updateProfile(plProfile);
		User userPl = userRepository.saveAndFlush(plUser);

		UUID pl = userPl.getId();
		Authority plAuthority = Authority.builder()
			.authType(AuthType.TEAM)
			.authTargetId(pl)
			.authorizerId("admin@gmail.com")
			.build();
		authRepository.saveAndFlush(plAuthority);
		userRepository.saveAndFlush(userPl);

	}

}
