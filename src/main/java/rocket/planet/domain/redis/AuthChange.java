package rocket.planet.domain.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@RedisHash("auth-change")
public class AuthChange {
	@Id
	private String email;
}
