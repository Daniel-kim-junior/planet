package rocket.planet.domain;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	@Rollback(false)
	void saveUser() {
		User adminUser = User.builder()
			.userPwd(passwordEncoder.encode("admin111!"))
			.userLock(false)
			.lastPwdModifiedDt(LocalDate.now())
			.userId("admin@gmail.com")
			.build();
		User saveAdmin = userRepository.save(adminUser);
		Profile adminProfile = Profile.builder()
			.profileBirthDt(LocalDate.of(1996, 8, 22))
			.role(Role.ADMIN)
			.userId(adminUser.getUserId())
			.profileStartDate(LocalDate.of(2021, 8, 22))
			.userName("어드민")
			.userNickName(Profile.idToUserNickName(adminUser.getUserId()))
			.profileDisplay(true)
			.profileCareer(1)
			.profileAnnualStatus(true)
			.build();
		profileRepository.save(adminProfile);
		saveAdmin.updateProfile(adminProfile);

		User crewUser = User.builder()
			.userPwd(passwordEncoder.encode("crew111!"))
			.userLock(false)
			.lastPwdModifiedDt(LocalDate.now())
			.userId("crew@gmail.com")
			.build();
		User saveCrew = userRepository.save(crewUser);
		Profile crewProfile = Profile.builder()
			.profileBirthDt(LocalDate.of(1994, 1, 17))
			.role(Role.CREW)
			.userId(crewUser.getUserId())
			.userName("김크루")
			.userNickName(Profile.idToUserNickName(crewUser.getUserId()))
			.profileStartDate(LocalDate.of(2021, 8, 22))
			.profileDisplay(true)
			.profileCareer(5)
			.profileAnnualStatus(true)
			.build();
		profileRepository.save(crewProfile);
		saveCrew.updateProfile(crewProfile);

		User pilotUser = User.builder()
			.userPwd(passwordEncoder.encode("pilot111!"))
			.userLock(false)
			.lastPwdModifiedDt(LocalDate.now())
			.userId("pilot@gmail.com")
			.build();
		User savePilot = userRepository.save(pilotUser);
		Profile pilotProfile = Profile.builder()
			.profileBirthDt(LocalDate.of(1994, 1, 17))
			.role(Role.PILOT)
			.userId(pilotUser.getUserId())
			.userName("파일럿")
			.profileStartDate(LocalDate.of(2021, 8, 22))
			.userNickName(Profile.idToUserNickName(pilotUser.getUserId()))
			.profileDisplay(true)
			.profileCareer(10)
			.profileAnnualStatus(true)
			.build();
		profileRepository.save(pilotProfile);
		savePilot.updateProfile(pilotProfile);

		User captainUser = User.builder()
			.userPwd(passwordEncoder.encode("captain111!"))
			.userLock(false)
			.lastPwdModifiedDt(LocalDate.now())
			.userId("captain@gmail.com")
			.build();
		User saveCaptain = userRepository.saveAndFlush(captainUser);
		Profile captainProfile = Profile.builder()
			.profileBirthDt(LocalDate.of(1999, 8, 23))
			.role(Role.CAPTAIN)
			.userId(captainUser.getUserId())
			.profileStartDate(LocalDate.of(2021, 8, 22))
			.userName("강캡틴")
			.userNickName(Profile.idToUserNickName(captainUser.getUserId()))
			.profileDisplay(true)
			.profileCareer(15)
			.profileAnnualStatus(false)
			.build();
		profileRepository.saveAndFlush(captainProfile);
		saveCaptain.updateProfile(captainProfile);

		User radarUser = User.builder()
			.userPwd(passwordEncoder.encode("radar111!"))
			.userLock(false)
			.lastPwdModifiedDt(LocalDate.now())
			.userId("radar@gmail.com")
			.build();
		User saveRadar = userRepository.save(radarUser);
		Profile radarProfile = Profile.builder()
			.profileBirthDt(LocalDate.of(1997, 12, 20))
			.role(Role.RADAR)
			.profileStartDate(LocalDate.of(2021, 8, 22))
			.userId(radarUser.getUserId())
			.userName("레이더")
			.userNickName(Profile.idToUserNickName(radarUser.getUserId()))
			.profileDisplay(true)
			.profileCareer(7)
			.profileAnnualStatus(true)
			.build();
		profileRepository.save(radarProfile);
		saveRadar.updateProfile(radarProfile);

		User plUser = User.builder()
			.userPwd(passwordEncoder.encode("plpl111!"))
			.userLock(false)
			.lastPwdModifiedDt(LocalDate.now())
			.userId("plpl@gmail.com")
			.build();
		User savePl = userRepository.save(plUser);
		Profile plProfile = Profile.builder()
			.profileBirthDt(LocalDate.of(1994, 7, 30))
			.role(Role.CREW)
			.userId(plUser.getUserId())
			.profileStartDate(LocalDate.of(2021, 8, 22))
			.userNickName(Profile.idToUserNickName(plUser.getUserId()))
			.userName("홍피엘")
			.profileDisplay(true)
			.profileCareer(3)
			.profileAnnualStatus(false)
			.build();
		profileRepository.save(plProfile);
		savePl.updateProfile(plProfile);

		User starUser = User.builder()
				.userPwd(passwordEncoder.encode("star111!"))
				.userLock(false)
				.lastPwdModifiedDt(LocalDate.now())
				.userId("star@gmail.com")
				.build();
		User saveStar = userRepository.save(starUser);
		Profile starProfile = Profile.builder()
				.profileBirthDt(LocalDate.of(1997, 11, 07))
				.role(Role.CREW)
				.userId(starUser.getUserId())
				.profileStartDate(LocalDate.of(2021, 8, 22))
				.userNickName(Profile.idToUserNickName(starUser.getUserId()))
				.userName("이별별")
				.profileDisplay(true)
				.profileCareer(2)
				.profileAnnualStatus(false)
				.build();
		profileRepository.save(starProfile);
		savePl.updateProfile(starProfile);

	}

}