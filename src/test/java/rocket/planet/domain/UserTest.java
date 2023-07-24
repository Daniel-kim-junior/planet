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
	@Rollback(value = false)
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
			.profileStatus(true)
			.profileAnnualStatus(true)
			.build();
		profileRepository.save(adminProfile);
		saveAdmin.updateProfile(adminProfile);

	}
}


