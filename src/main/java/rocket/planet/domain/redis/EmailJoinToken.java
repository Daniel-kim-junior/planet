package rocket.planet.domain.redis;

import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;

@RedisHash(value = "email-join-token")
public class EmailJoinToken extends EmailToken {

	@Builder
	EmailJoinToken(String email, String token) {
		super(email, token);
	}
}