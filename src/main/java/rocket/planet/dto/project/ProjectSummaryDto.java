package rocket.planet.dto.project;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ProjectSummaryDto {
	private String projectName;
	private List<String> projectMember;
	private String projectLeader;
	private LocalDate projectStartDt;
	private LocalDate projectEndDt;
	private String projectStatus;
}
