package rocket.planet.service.stats;

import lombok.Builder;

public class TechStats extends StatCategory {
	@Builder
	public TechStats(String name) {
		super(name);
	}
}
