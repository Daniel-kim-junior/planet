package rocket.planet.domain.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@RedisHash(value = "access-token", timeToLive = 60 * 10)
public class AccessToken {

	@Id
	private String email;

	private String token;

}
