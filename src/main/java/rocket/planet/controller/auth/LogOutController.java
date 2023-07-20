package rocket.planet.controller.auth;

import static rocket.planet.dto.auth.AuthDto.*;

import org.springframework.http.ResponseEntity;
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

	private final static String LOGOUT = "로그아웃 되었습니다";

	@PostMapping("/api/logout")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN', 'ROLE_RADAR', 'ROLE_CAPTAIN', 'ROLE_PILOT', 'ROLE_CREW', 'ROLE_GUEST', 'ROLE_PL')")
	public ResponseEntity<LogOutResDto> logOut(@RequestHeader("Authorization") String token,
		@AuthenticationPrincipal(expression = "user") User user) throws RedisException {
		refreshTokenRedisRepository.deleteById(user.getUserId());
		accessTokenRedisRepository.deleteById(user.getUserId());
		SecurityContextHolder.clearContext();
		return ResponseEntity.ok().body(LogOutResDto.builder().message(LOGOUT).build());
	}
}
