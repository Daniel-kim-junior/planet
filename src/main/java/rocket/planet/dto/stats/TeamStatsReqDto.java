package rocket.planet.dto.stats;

import static lombok.AccessLevel.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class TeamStatsReqDto {

	private String deptName;

	private String teamName;

	private int unit;

	@Builder
	public TeamStatsReqDto(String deptName, String teamName, int unit) {
		this.deptName = deptName;
		this.teamName = teamName;
		this.unit = unit;
	}
}
