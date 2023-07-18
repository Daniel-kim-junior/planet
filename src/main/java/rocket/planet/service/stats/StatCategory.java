package rocket.planet.service.stats;

import lombok.Getter;

@Getter
public abstract class StatCategory {
	private String name;

	public StatCategory(String name) {
		this.name = name;
	}

}
