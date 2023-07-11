package rocket.planet.dto.project;

import lombok.Builder;
import lombok.Getter;
import rocket.planet.domain.AuthType;

@Getter
@Builder
public class ProjectDeleteDto {
	private String projectName;
	private AuthType authType;
}
