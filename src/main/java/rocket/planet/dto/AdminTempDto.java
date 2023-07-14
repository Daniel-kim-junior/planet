package rocket.planet.controller.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminDto {

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
