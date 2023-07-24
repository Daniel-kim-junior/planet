package rocket.planet.domain.redis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@Getter
@RedisHash("last-login")
public class LastLogin {
	@Id
	private String email;

	private List<String> loginLogs = new ArrayList<>();

	@Builder
	public LastLogin(String email) {
		this.email = email;
	}

	public void add(String log) {
		loginLogs.add(log);
	}

}