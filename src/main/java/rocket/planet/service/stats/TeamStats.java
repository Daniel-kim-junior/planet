package rocket.planet.service.stats;

import lombok.Builder;

public class TeamStats extends StatCategory {

	@Builder
	public TeamStats(String name) {
		super(name);
	}
}
