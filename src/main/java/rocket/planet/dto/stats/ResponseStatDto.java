package rocket.planet.dto.stats;

import static lombok.AccessLevel.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = PROTECTED)
@ToString
public class ResponseStatDto {

	private String type;

	private String name;

	private LabelAndStatDto labelAndStats;

	@Builder
	public ResponseStatDto(String type, String name, LabelAndStatDto labelAndStats) {
		this.type = type;
		this.name = name;
		this.labelAndStats = labelAndStats;
	}
}
