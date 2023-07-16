package rocket.planet.dto.admin;

import lombok.Builder;
import lombok.Getter;

public class AdminDeptTeamDto {

	@Getter
	@Builder
	public static class AdminDeptAddReqDto {
		private String name;

		private String deptType;

	}

	@Getter
	@Builder
	public static class AdminDeptModReqDto {
		private String targetName;

		private String changeName;

	}

	@Getter
	@Builder
	public static class AdminTeamModReqDto {
		private String targetName;

		private String changeName;

		private String changeDesc;

	}

	@Getter
	@Builder
	public static class AdminDeptTeamDelReqDto {
		private String name;

	}

	@Getter
	@Builder
	public static class AdminTeamAddReqDto {
		private String teamName;

		private String deptName;

		private String teamDesc;
	}

	@Getter
	@Builder
	public static class AdminResDto {

		private String message;

	}

}
