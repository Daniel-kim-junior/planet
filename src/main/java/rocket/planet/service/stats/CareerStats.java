package rocket.planet.service.stats;

import lombok.Builder;

public class CareerStats extends StatCategory {

	@Builder
	public CareerStats(String name) {
		super(name);
	}
}
