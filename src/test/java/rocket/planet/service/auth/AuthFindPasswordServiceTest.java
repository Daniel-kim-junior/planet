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
import rocket.planet.dto.auth.PasswordModifyReqDto;
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

		/**
		 * 비밀번호 변경 확인 토큰이 없을때
		 */
		assertThrows(NoValidEmailTokenException.class, () -> {
			authFindPasswordService.modifyPassword(PasswordModifyReqDto.builder().id("noValidTokenUser@gmail.com")
				.password("Qwer1234!").build());
		});
		/**
		 * 비밀번호 변경 확인 토큰이 있지만 바꾸려는 비밀번호가 예전 비밀번호와 같을 때
		 */
		assertThrows(PasswordMatchException.class, () -> {
			authFindPasswordService.modifyPassword(PasswordModifyReqDto.builder().id("admin@gmail.com")
				.password("Qwer1234!").build());
		});
		/**
		 * 비밀번호 변경 확인 토큰이 있고 바꾸려는 비밀번호가 예전 비밀번호와 다를 때
		 */
		Assertions.assertThat(
			authFindPasswordService.modifyPassword(PasswordModifyReqDto.builder().id("admin@gmail.com")
				.password("Qwer12345!").build())).isEqualTo("비밀번호가 변경되었습니다");

	}

}