package rocket.planet.dto.stats;

import static lombok.AccessLevel.*;

import javax.validation.constraints.Min;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class DeptStatsReqDto {

	private String deptName;

	@Min(1)
	private int unit;

	@Builder
	public DeptStatsReqDto(String deptName, int unit) {
		this.deptName = deptName;
		this.unit = unit;
	}
}
