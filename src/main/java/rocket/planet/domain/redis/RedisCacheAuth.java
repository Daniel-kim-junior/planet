package rocket.planet.domain.redis;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import rocket.planet.domain.AuthType;

@Builder
@Getter
public class RedisCacheAuth {
	private UUID authorityTargetUid;

	private AuthType authorityTargetTable;

}
