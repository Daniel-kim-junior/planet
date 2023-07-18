package rocket.planet.dto.stats;

import static lombok.AccessLevel.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class EntireStatsReqDto {

	private String companyName;

	private int unit;

	@Builder
	public EntireStatsReqDto(String companyName, int unit) {
		this.companyName = companyName;
		this.unit = unit;
	}
}
