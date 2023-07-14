package rocket.planet.domain.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@RedisHash(value = "refresh-token", timeToLive = 60 * 30)
public class RefreshToken {
	@Id
	private String email;

	private String token;

}
