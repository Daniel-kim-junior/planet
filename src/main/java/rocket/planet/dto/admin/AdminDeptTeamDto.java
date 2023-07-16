package rocket.planet.dto.admin;

import static lombok.AccessLevel.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminDeptTeamDto {

	@Getter
	@NoArgsConstructor(access = PROTECTED)
	public static class AdminDeptAddReqDto {
		private String name;

		private String deptType;

		@Builder
		public AdminDeptAddReqDto(String name, String deptType) {
			this.name = name;
			this.deptType = deptType;
		}
	}

	@Getter
	@NoArgsConstructor(access = PROTECTED)
	public static class AdminDeptModReqDto {
		private String targetName;

		private String changeName;

		@Builder
		public AdminDeptModReqDto(String targetName, String changeName) {
			this.targetName = targetName;
			this.changeName = changeName;
		}
	}

	@Getter
	@NoArgsConstructor(access = PROTECTED)
	public static class AdminTeamModReqDto {
		private String targetName;

		private String changeName;

		private String changeDesc;

		@Builder
		public AdminTeamModReqDto(String targetName, String changeName, String changeDesc) {
			this.targetName = targetName;
			this.changeName = changeName;
			this.changeDesc = changeDesc;
		}
	}

	@Getter
	@NoArgsConstructor(access = PROTECTED)
	public static class AdminDeptTeamDelReqDto {
		private String name;

		@Builder
		public AdminDeptTeamDelReqDto(String name) {
			this.name = name;
		}
	}

	@Getter
	@NoArgsConstructor(access = PROTECTED)
	public static class AdminTeamAddReqDto {
		private String teamName;

		private String deptName;

		private String teamDesc;

		@Builder
		public AdminTeamAddReqDto(String teamName, String deptName, String teamDesc) {
			this.teamName = teamName;
			this.deptName = deptName;
			this.teamDesc = teamDesc;
		}
	}

	@Getter
	@Builder
	public static class AdminResDto {

		private String message;

	}

}
