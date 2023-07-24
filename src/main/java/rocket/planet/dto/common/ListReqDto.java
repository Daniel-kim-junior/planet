package rocket.planet.dto.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ListReqDto {

	@Builder.Default
	private Integer page = 1;

	@Builder.Default
	private Integer pageSize = 8;

	public Integer getPage() {
		// page = page - 1;
		if (page < 0) {
			page = 0;
		}
		return page;
	}
}
