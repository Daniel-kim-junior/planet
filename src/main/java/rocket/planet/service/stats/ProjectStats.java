package rocket.planet.service.stats;

import lombok.Builder;

public class ProjectStats extends StatCategory {

	@Builder
	public ProjectStats(String name) {
		super(name);
	}
}
