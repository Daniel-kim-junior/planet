package rocket.planet.dto.project;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProjectDto {
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class NameReqDto {
		private String name;

		@Builder
		public NameReqDto(String name) {
			this.name = name;
		}
	}
}
