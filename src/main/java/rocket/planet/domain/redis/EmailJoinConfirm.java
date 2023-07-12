package rocket.planet.domain.redis;

import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;

@RedisHash(value = "email-join-confirm")
public class EmailJoinConfirm extends EmailConfirm {
	@Builder
	protected EmailJoinConfirm(String email) {
		super(email);
	}
}
