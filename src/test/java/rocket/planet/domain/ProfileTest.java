//package rocket.planet.domain;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import rocket.planet.repository.jpa.ProfileRepository;
//import rocket.planet.repository.jpa.UserRepository;
//
//import java.time.LocalDate;
//
//@SpringBootTest
//public class ProfileTest {
//	@Autowired
//	private UserRepository userRepository;
//
//	@Autowired
//	private ProfileRepository profileRepository;
//
//	@Test
//	@Rollback(false)
//	void saveUser() {
//
//		Profile adminProfile = Profile.builder()
//			.profileBirthDt(LocalDate.of(1995, 3, 22))
//			.profileDisplay(true)
//			.profileCareer(1)
//			.profileAnnualStatus(true)
//			.build();
//
//		profileRepository.saveAndFlush(adminProfile);
//
//		Profile crewProfile = Profile.builder()
//			.profileBirthDt(LocalDate.of(1996, 8, 22))
//			.profileDisplay(true)
//			.profileCareer(5)
//			.profileAnnualStatus(true)
//			.build();
//		profileRepository.saveAndFlush(crewProfile);
//
//		Profile pilotProfile = Profile.builder()
//			.profileBirthDt(LocalDate.of(1994, 1, 17))
//			.profileDisplay(true)
//			.profileCareer(10)
//			.profileAnnualStatus(true)
//			.build();
//		profileRepository.saveAndFlush(pilotProfile);
//
//		Profile captainProfile = Profile.builder()
//			.profileBirthDt(LocalDate.of(1999, 8, 23))
//			.profileDisplay(true)
//			.profileCareer(15)
//			.profileAnnualStatus(false)
//			.build();
//
//		profileRepository.saveAndFlush(captainProfile);
//
//		Profile radarProfile = Profile.builder()
//			.profileBirthDt(LocalDate.of(1997, 12, 20))
//			.profileDisplay(true)
//			.profileCareer(7)
//			.profileAnnualStatus(true)
//			.build();
//
//		profileRepository.saveAndFlush(radarProfile);
//
//		Profile plProfile = Profile.builder()
//			.profileBirthDt(LocalDate.of(1998, 12, 5))
//			.profileDisplay(true)
//			.profileCareer(3)
//			.profileAnnualStatus(false)
//			.build();
//		profileRepository.saveAndFlush(plProfile);
//
//
//	}
//
//}
