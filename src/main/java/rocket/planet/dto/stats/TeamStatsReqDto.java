package rocket.planet.dto.stats;

import static lombok.AccessLevel.*;

import javax.validation.constraints.Min;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class TeamStatsReqDto {

	private String deptName;

	private String teamName;

	@Min(1)
	private int unit;

	@Builder
	public TeamStatsReqDto(String deptName, String teamName, int unit) {
		this.deptName = deptName;
		this.teamName = teamName;
		this.unit = unit;
	}
}
