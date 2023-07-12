package rocket.planet.domain.redis;

import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;

@RedisHash(value = "email-find-token")
public class EmailFindToken extends EmailToken {

	@Builder
	EmailFindToken(String email, String token) {
		super(email, token);
	}
}