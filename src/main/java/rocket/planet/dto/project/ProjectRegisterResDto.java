package rocket.planet.dto.project;

import lombok.Builder;
import lombok.Getter;
import rocket.planet.domain.ProjectStatus;

@Getter
@Builder
public class ProjectRegisterResDto {
	private String projectName;
	private ProjectStatus projectStatus;
}
