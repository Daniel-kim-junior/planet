package rocket.planet.dto.admin;

import static lombok.AccessLevel.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rocket.planet.util.annotation.ValidDept;

public class AdminDeptTeamDto {

	@Getter
	@NoArgsConstructor(access = PROTECTED)
	public static class AdminDeptAddReqDto {

		@NotEmpty
		@NotBlank
		private String name;

		@ValidDept
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

		@NotBlank
		@NotEmpty
		private String targetName;

		@NotBlank
		@NotEmpty
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

		@NotBlank
		@NotEmpty
		private String targetName;

		@NotBlank
		@NotEmpty
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
	public static class AdminTeamAddReqDto {

		@NotBlank
		@NotEmpty
		private String teamName;

		@NotBlank
		@NotNull
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

	@Getter
	@NoArgsConstructor(access = PROTECTED)
	public static class UpdateDeptActiveReqDto {
		private String deptName;

		@Builder
		public UpdateDeptActiveReqDto(String deptName) {
			this.deptName = deptName;
		}
	}

	@Getter
	@NoArgsConstructor(access = PROTECTED)
	public static class UpdateTeamActiveReqDto {
		private String teamName;

		@Builder
		public UpdateTeamActiveReqDto(String teamName) {
			this.teamName = teamName;
		}
	}

}
