package rocket.planet.redis.email;

import static lombok.AccessLevel.*;

import java.util.concurrent.TimeUnit;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
 * 이메일 토큰 Redis Entity
 */
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@ToString
@RedisHash
public class EmailToken {
	@Id
	private String id;
	private String random;
	@TimeToLive(unit = TimeUnit.MILLISECONDS)
	private Long timeToLive;
}
