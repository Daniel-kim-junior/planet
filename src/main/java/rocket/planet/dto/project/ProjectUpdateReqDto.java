package rocket.planet.dto.project;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectUpdateReqDto {
	private String userName;
	private String projectName;
	private String projectDesc;
	private String projectTech;
	private LocalDate projectStartDt;
	private LocalDate projectEndDt;
}
