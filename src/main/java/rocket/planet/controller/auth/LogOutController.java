package rocket.planet.controller.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import rocket.planet.repository.redis.RefreshTokenRedisRepository;
import rocket.planet.util.security.UserDetailsImpl;

@RestController
@RequiredArgsConstructor
public class LogOutController {

	private final RefreshTokenRedisRepository refreshTokenRedisRepository;

	@PostMapping("/api/logout")
	public String logOut(@RequestHeader("Authorization") String token) throws RedisException {
		refreshTokenRedisRepository.deleteById(UserDetailsImpl.getLoginUserId());
		SecurityContextHolder.clearContext();
		return "LOGOUT";
	}
}
