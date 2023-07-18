package rocket.planet.dto.project;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectNameReqDto {
	private String name;
	private String userNickName;
	private String role;

	@Builder
	public ProjectNameReqDto(String name, String userNickName, String role) {
		this.name = name;
		this.userNickName = userNickName;
		this.role = role;
	}
}
