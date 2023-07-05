package rocket.planet.dto.project;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import rocket.planet.domain.ProjectStatus;

@Getter
@Builder
public class ProjectRegisterReqDto {
	private String userName;
	private String projectName;
	private String projectDesc;
	private String projectTech;
	private LocalDate projectStartDt;
	private LocalDate projectEndDt;
	private ProjectStatus projectStatus;
}
