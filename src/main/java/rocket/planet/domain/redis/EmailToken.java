package rocket.planet.domain.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@RedisHash(value = "email-token", timeToLive = 60 * 60 * 3)
public class EmailToken {
	@Id
	private String email;

	private String token;



}