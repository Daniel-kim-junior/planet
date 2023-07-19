package rocket.planet.dto.stats;

import static lombok.AccessLevel.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class TeamStatDto {

	private String name;

	private Long count;

	@Builder
	public TeamStatDto(String name, Long count) {
		this.name = name;
		this.count = count;
	}
}
