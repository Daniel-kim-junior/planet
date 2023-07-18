package rocket.planet.dto.stats;

import static lombok.AccessLevel.*;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class LabelAndStatDto {

	private Map<String, Double> data;

	@Builder
	public LabelAndStatDto(Map<String, Double> data) {
		this.data = data;
	}

}
