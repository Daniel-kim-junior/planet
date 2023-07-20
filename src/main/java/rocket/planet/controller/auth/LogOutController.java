package rocket.planet.controller.auth;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import rocket.planet.domain.User;
import rocket.planet.repository.redis.AccessTokenRedisRepository;
import rocket.planet.repository.redis.RefreshTokenRedisRepository;

@RestController
@RequiredArgsConstructor
public class LogOutController {

	private final RefreshTokenRedisRepository refreshTokenRedisRepository;

	private final AccessTokenRedisRepository accessTokenRedisRepository;

	@PostMapping("/api/logout")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN', 'ROLE_RADAR', 'ROLE_CAPTAIN', 'ROLE_PILOT', 'ROLE_CREW', 'ROLE_GUEST', 'ROLE_PL')")
	public String logOut(@RequestHeader("Authorization") String token,
		@AuthenticationPrincipal(expression = "user") User user) throws RedisException {
		refreshTokenRedisRepository.deleteById(user.getUserId());
		accessTokenRedisRepository.deleteById(user.getUserId());
		SecurityContextHolder.clearContext();
		return "LOGOUT";
	}
}
