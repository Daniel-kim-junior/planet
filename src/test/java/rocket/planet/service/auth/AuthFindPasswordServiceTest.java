package rocket.planet.service.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.domain.User;
import rocket.planet.domain.redis.EmailFindConfirm;
import rocket.planet.dto.auth.AuthDto;
import rocket.planet.dto.auth.AuthDto.PasswordModifyReqDto;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.repository.redis.EmailFindConfirmRepository;
import rocket.planet.util.exception.NoValidEmailTokenException;
import rocket.planet.util.exception.PasswordMatchException;

@SpringBootTest
@Transactional
class AuthFindPasswordServiceTest {

	@InjectMocks
	private AuthFindPasswordService authFindPasswordService;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private UserRepository userRepository;

	@Mock
	private EmailFindConfirmRepository emailFindConfirmRepository;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		User adminUser = User.builder()
			.userPwd("Qwer1234!")
			.userLock(false)
			.lastPwdModifiedDt(LocalDate.now())
			.userId("admin@gmail.com")
			.build();

		when(userRepository.findByUserId("admin@gmail.com")).thenReturn(Optional.of(adminUser));
		when(emailFindConfirmRepository.findById("admin@gmail.com"))
			.thenReturn(Optional.of(EmailFindConfirm.builder().email("").build()));
		when(passwordEncoder.matches(eq("Qwer1234!"), any())).thenReturn(true);
		when(passwordEncoder.matches(eq("Qwer12345!"), any())).thenReturn(false);

	}

	@Test
	void 비밀번호_변경_테스트() throws Exception {

		assertThrows(NoValidEmailTokenException.class, () -> {
			authFindPasswordService.modifyPassword(PasswordModifyReqDto.builder().id("noValidTokenUser@gmail.com")
				.password("Qwer1234!").build());
		});

		assertThrows(PasswordMatchException.class, () -> {
			authFindPasswordService.modifyPassword(PasswordModifyReqDto.builder().id("admin@gmail.com")
				.password("Qwer1234!").build());
		});

		Assertions.assertThat(
				authFindPasswordService.modifyPassword(PasswordModifyReqDto.builder().id("admin@gmail.com")
					.password("Qwer12345!").build()))
			.isInstanceOf(AuthDto.PasswordModifyResDto.class);

	}

}