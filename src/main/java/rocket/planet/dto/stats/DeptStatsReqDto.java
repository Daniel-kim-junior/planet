package rocket.planet.dto.stats;

import static lombok.AccessLevel.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class DeptStatsReqDto {

	@NotBlank
	@NotEmpty
	private String deptName;

	@Min(1)
	private int unit;

	@Builder
	public DeptStatsReqDto(String deptName, int unit) {
		this.deptName = deptName;
		this.unit = unit;
	}
}
