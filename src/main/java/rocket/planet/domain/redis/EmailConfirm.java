package rocket.planet.domain.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@RedisHash(value = "email-confirm", timeToLive = 60 * 60 * 24)
public class EmailConfirm {
	@Id
	private String email;
}