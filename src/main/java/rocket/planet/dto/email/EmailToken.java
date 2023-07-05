package rocket.planet.dto.email;

import static lombok.AccessLevel.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Builder
@ToString
@RedisHash("emailToken")
public class EmailToken {
	@Id
	private String id;
	private String randomString;

	private boolean isVerified;

	public EmailToken verifiedToken() {
		this.isVerified = true;
		return this;
	}
}
