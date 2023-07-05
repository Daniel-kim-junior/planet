package rocket.planet.dto.email;

import java.util.concurrent.TimeUnit;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@RedisHash
public class EmailToken {

	@Id
	private String id;
	private String random;
	@TimeToLive(unit = TimeUnit.MINUTES)
	private Long timeToLive;
}
