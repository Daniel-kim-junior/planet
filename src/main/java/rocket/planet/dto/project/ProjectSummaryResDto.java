package rocket.planet.dto.project;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectSummaryResDto {
	private String projectName;
	private List<String> projectMember;
	private String projectLeader;
	private LocalDate projectStartDt;
	private LocalDate projectEndDt;
	private String projectStatus;

	@Builder
	public ProjectSummaryResDto(String projectName, List<String> projectMember, String projectLeader,
		LocalDate projectStartDt, LocalDate projectEndDt, String projectStatus) {
		this.projectName = projectName;
		this.projectMember = projectMember;
		this.projectLeader = projectLeader;
		this.projectStartDt = projectStartDt;
		this.projectEndDt = projectEndDt;
		this.projectStatus = projectStatus;
	}
}
