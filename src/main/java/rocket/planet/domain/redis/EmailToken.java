package rocket.planet.domain.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;

@Getter
@RedisHash(timeToLive = 60 * 60 * 3)
public abstract class EmailToken {
	@Id
	private String email;

	private String token;

	protected EmailToken(String email, String token) {
		this.email = email;
		this.token = token;
	}

}
