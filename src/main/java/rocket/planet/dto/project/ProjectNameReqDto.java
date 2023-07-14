package rocket.planet.dto.project;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectNameReqDto {
	private String name;
	private String userNickName;
	private String role;
}
