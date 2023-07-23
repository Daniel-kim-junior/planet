package rocket.planet.dto.project;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProjectSummaryResDto {
	private String projectName;
	private List<String> projectMember;
	private String projectLeader;
	private LocalDate projectStartDt;
	private LocalDate projectEndDt;
	private String projectStatus;

}
