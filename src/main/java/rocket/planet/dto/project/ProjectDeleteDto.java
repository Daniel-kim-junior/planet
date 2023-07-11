package rocket.planet.dto.project;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectDeleteDto {
	private String projectName;
	private String userNickName;
	private String authType;
}
