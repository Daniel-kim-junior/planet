package rocket.planet.domain.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@RedisHash(value = "profile-visitant", timeToLive = 60 * 60 * 24 * 15)
public class ProfileVisitor {
	@Id
	private String id;

	private String role;
}
