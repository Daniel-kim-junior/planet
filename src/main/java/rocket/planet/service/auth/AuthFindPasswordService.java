package rocket.planet.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import rocket.planet.domain.redis.EmailConfirm;
import rocket.planet.domain.redis.EmailFindConfirm;
import rocket.planet.dto.auth.PasswordModifyReqDto;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.repository.redis.EmailConfirmRepository;
import rocket.planet.util.exception.NoValidEmailTokenException;

@Service
@RequiredArgsConstructor
public class AuthFindPasswordService {

	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	private final EmailConfirmRepository emailConfirmRepository;

	private final static String SUCCESS = "비밀번호가 변경되었습니다";

	@Transactional
	public String modifyPassword(PasswordModifyReqDto dto) throws Exception {
		EmailConfirm emailConfirm = emailConfirmRepository.findById(dto.getId())
			.orElseThrow(() -> new NoValidEmailTokenException());

		if (emailConfirm instanceof EmailFindConfirm) {
			userRepository.findByUserId(dto.getId()).ifPresent(user -> {
				user.updatePassword(passwordEncoder.encode(dto.getPassword()));
			});
			return SUCCESS;
		}
		throw new NoValidEmailTokenException();
	}
}
