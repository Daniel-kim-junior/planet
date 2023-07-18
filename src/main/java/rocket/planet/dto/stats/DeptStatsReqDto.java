package rocket.planet.dto.stats;

import static lombok.AccessLevel.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class DeptStatsReqDto {

	private String deptName;

	private int unit;

	@Builder
	public DeptStatsReqDto(String deptName, int unit) {
		this.deptName = deptName;
		this.unit = unit;
	}
}
