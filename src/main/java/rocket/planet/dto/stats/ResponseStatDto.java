package rocket.planet.dto.stats;

import static lombok.AccessLevel.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ResponseStatDto {

	private String name;

	private LabelAndStatDto labelAndStats;

	@Builder
	public ResponseStatDto(String name, LabelAndStatDto labelAndStats) {
		this.name = name;
		this.labelAndStats = labelAndStats;
	}
}
