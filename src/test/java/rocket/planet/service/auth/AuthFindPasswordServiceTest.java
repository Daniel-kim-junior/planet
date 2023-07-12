package rocket.planet.service.auth;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.domain.User;
import rocket.planet.repository.jpa.UserRepository;

@SpringBootTest
@Transactional
class AuthFindPasswordServiceTest {

	@InjectMocks
	private AuthFindPasswordService authFindPasswordService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		User adminUser = User.builder()
			.userPwd("encodedPassword")
			.userLock(false)
			.lastPwdModifiedDt(LocalDate.now())
			.userId("admin@gmail.com")
			.build();

		when(userRepository.findByUserId("admin@gmail.com")).thenReturn(Optional.of(adminUser));
		doAnswer(invocation -> {
			adminUser.updatePassword(passwordEncoder.encode("admin222!"));
			return null;
		}).when(userRepository).save(adminUser);
	}

	@Test
	void 비밀번호_변경_테스트() throws Exception {
		User adminUser = User.builder()
			.userPwd("admin111!")
			.userLock(false)
			.lastPwdModifiedDt(LocalDate.now())
			.userId("admin@gmail.com")
			.build();

		userRepository.save(adminUser);

		Optional<User> findUser = userRepository.findByUserId("admin@gmail.com");

		passwordEncoder.matches("admin222!", findUser.get().getUserPwd());

	}

}