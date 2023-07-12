package rocket.planet.domain.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;

@Getter
@RedisHash(timeToLive = 60 * 60 * 24)
public abstract class EmailConfirm {
	@Id
	private String email;

	protected EmailConfirm(String email) {
		this.email = email;
	}
}