package rocket.planet.dto.project;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProjectUpdateDto {

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class ProjectUpdateReqDto {
		private String projectName;
		private String userNickName;
		private String authType;

	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ProjectUpdateDetailDto {
		private String userNickName;
		private String projectName;
		private String projectDesc;
		private String projectTech;
		private LocalDate projectStartDt;
		private LocalDate projectEndDt;
		private String projectLeader;
		private List<String> projectMember;
		private String authType;

		@Builder
		public ProjectUpdateDetailDto(String userNickName, String projectName, String projectDesc, String projectTech,
			LocalDate projectStartDt, LocalDate projectEndDt, String projectLeader, List<String> projectMember,
			String authType) {
			this.userNickName = userNickName;
			this.projectName = projectName;
			this.projectDesc = projectDesc;
			this.projectTech = projectTech;
			this.projectStartDt = projectStartDt;
			this.projectEndDt = projectEndDt;
			this.projectLeader = projectLeader;
			this.projectMember = projectMember;
			this.authType = authType;
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ProjectUpdateStatusDto {
		private String projectName;
		private String userNickName;
		private String authType;

		@Builder
		public ProjectUpdateStatusDto(String projectName, String userNickName, String authType) {
			this.projectName = projectName;
			this.userNickName = userNickName;
			this.authType = authType;
		}
	}

}