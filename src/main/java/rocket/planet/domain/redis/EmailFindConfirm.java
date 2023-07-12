package rocket.planet.domain.redis;

import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;

@RedisHash(value = "email-find-confirm")
public class EmailFindConfirm extends EmailConfirm {
	@Builder
	public EmailFindConfirm(String email) {
		super(email);
	}
}
