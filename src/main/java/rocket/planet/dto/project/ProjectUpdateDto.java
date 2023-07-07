package rocket.planet.dto.project;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

public class ProjectUpdateDto {

	@Getter
	@Builder
	public static class ProjectUpdateReqDto {
		private String projectName;
		private String userName;
	}

	@Getter
	@Builder
	public static class ProjectUpdateDetailDto {
		private String userName;
		private String projectName;
		private String projectDesc;
		private String projectTech;
		private LocalDate projectStartDt;
		private LocalDate projectEndDt;
	}

}