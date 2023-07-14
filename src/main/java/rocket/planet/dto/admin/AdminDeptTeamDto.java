package rocket.planet.dto.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminDeptTeamDto {

	@Getter
	@NoArgsConstructor
	public static class AdminReqDto {
		private String name;

		private String deptType;

	}

	@Getter
	@Builder
	public static class AdminResDto {

		private String message;

	}

}
