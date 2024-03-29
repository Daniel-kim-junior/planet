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
	@Builder
	@AllArgsConstructor
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

	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	@Builder
	public static class ProjectUpdateStatusDto {
		private String projectName;
		private String userNickName;
		private String authType;

	}

}