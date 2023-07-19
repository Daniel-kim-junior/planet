package rocket.planet.service.stats;

import lombok.Builder;

public class DeptStats extends StatCategory {
	@Builder
	public DeptStats(String name) {
		super(name);
	}
}
