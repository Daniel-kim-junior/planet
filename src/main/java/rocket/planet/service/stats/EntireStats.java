package rocket.planet.service.stats;

import static lombok.AccessLevel.*;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
public class EntireStats {

	private String companyName;

	@Builder
	public EntireStats(String companyName) {
		this.companyName = companyName;
	}
}
