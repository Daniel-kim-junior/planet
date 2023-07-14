package rocket.planet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminTempDto {

	@Getter
	@NoArgsConstructor
	public static class AdminReqDto {
		private String name;

	}

	@Getter
	@Builder
	public static class AdminResDto {

		private String message;

	}

}
