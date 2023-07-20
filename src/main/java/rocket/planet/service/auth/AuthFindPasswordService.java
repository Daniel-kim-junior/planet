package rocket.planet.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Builder;
import rocket.planet.domain.redis.EmailFindConfirm;
import rocket.planet.dto.auth.AuthDto.PasswordModifyReqDto;
import rocket.planet.dto.auth.AuthDto.PasswordModifyResDto;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.repository.redis.EmailFindConfirmRepository;
import rocket.planet.util.exception.NoValidEmailTokenException;
import rocket.planet.util.exception.PasswordMatchException;

@Service
public class AuthFindPasswordService {

	private PasswordEncoder passwordEncoder;

	private UserRepository userRepository;

	private EmailFindConfirmRepository emailFindConfirmRepository;

	private final static String SUCCESS = "비밀번호가 변경되었습니다";

	@Builder
	public AuthFindPasswordService(PasswordEncoder passwordEncoder, UserRepository userRepository,
		EmailFindConfirmRepository emailFindConfirmRepository) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.emailFindConfirmRepository = emailFindConfirmRepository;
	}

	@Transactional
	public PasswordModifyResDto modifyPassword(PasswordModifyReqDto dto) throws Exception {

		EmailFindConfirm emailConfirm = emailFindConfirmRepository.findById(dto.getId())
			.orElseThrow(() -> new NoValidEmailTokenException());

		userRepository.findByUserId(dto.getId()).ifPresent(user -> {
			if (passwordEncoder.matches(dto.getPassword(), user.getUserPwd()))
				throw new PasswordMatchException();
			user.updatePassword(passwordEncoder.encode(dto.getPassword()));
		});

		emailFindConfirmRepository.delete(emailConfirm);
		return PasswordModifyResDto.builder().message(SUCCESS).build();
	}
}
