package rocket.planet.domain.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@RedisHash(value = "refresh-token", timeToLive = 60 * 30)
public class RefreshToken {
	@Id
	private String email;

	private String token;

	private List<RedisCacheAuth> authorities;
}
