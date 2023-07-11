package rocket.planet.service.auth;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rocket.planet.controller.auth.PasswordModifyReqDto;

@Service
@RequiredArgsConstructor
public class AuthFindPasswordService {

	// private final UserRepository userRepository;
	// private final PasswordEncoder passwordEncoder;

	public String modifyPassword(PasswordModifyReqDto dto) {
		// dto.getId()
		return null;
	}
}
