package rocket.planet.dto.project;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectDetailResDto {
	private String projectName;
	private String projectLeader;
	private String team;
	private String dept;
	private List<String> projectMember;
	private String projectStatus;
	private LocalDate projectStartDt;
	private LocalDate projectEndDt;
	private String projectTech;
	private String projectDesc;
	private String projectLastModifiedBy;
	private LocalDate lastModifiedDate;

}
