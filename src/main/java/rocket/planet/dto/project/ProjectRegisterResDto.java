package rocket.planet.dto.project;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rocket.planet.domain.ProjectStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectRegisterResDto {
	private String projectName;
	private ProjectStatus projectStatus;

	@Builder
	public ProjectRegisterResDto(String projectName, ProjectStatus projectStatus) {
		this.projectName = projectName;
		this.projectStatus = projectStatus;
	}
}
