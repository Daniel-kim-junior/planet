package rocket.planet.dto.project;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

	@Builder
	public ProjectDetailResDto(String projectName, String projectLeader, String team, String dept,
		List<String> projectMember, String projectStatus, LocalDate projectStartDt, LocalDate projectEndDt,
		String projectTech, String projectDesc, String projectLastModifiedBy, LocalDate lastModifiedDate) {
		this.projectName = projectName;
		this.projectLeader = projectLeader;
		this.team = team;
		this.dept = dept;
		this.projectMember = projectMember;
		this.projectStatus = projectStatus;
		this.projectStartDt = projectStartDt;
		this.projectEndDt = projectEndDt;
		this.projectTech = projectTech;
		this.projectDesc = projectDesc;
		this.projectLastModifiedBy = projectLastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
	}
}
