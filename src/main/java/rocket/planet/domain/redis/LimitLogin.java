package rocket.planet.domain.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@RedisHash(value = "limit-login", timeToLive = 60 * 30)
public class LimitLogin {
	@Id
	private String email;
	private int count;

	public int add() {
		count++;
		return count;
	}
}
