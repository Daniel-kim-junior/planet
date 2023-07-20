package rocket.planet.dto.stats;

import static lombok.AccessLevel.*;

import javax.validation.constraints.Min;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class EntireStatsReqDto {

	private String companyName;

	@Min(1)
	private int unit;

	@Builder
	public EntireStatsReqDto(String companyName, int unit) {
		this.companyName = companyName;
		this.unit = unit;
	}
}
