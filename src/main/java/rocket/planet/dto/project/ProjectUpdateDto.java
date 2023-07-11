package rocket.planet.dto.project;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

public class ProjectUpdateDto {

	@Getter
	@Builder
	public static class ProjectUpdateReqDto {
		private String projectName;
		private String userNickName;
		private String authType;
	}

	@Getter
	@Builder
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

}