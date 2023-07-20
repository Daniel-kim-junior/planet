package rocket.planet.dto.stats;

import static lombok.AccessLevel.*;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = PROTECTED)
@ToString
public class LabelAndStatDto {

	private Map<String, Integer> data;

	@Builder
	public LabelAndStatDto(Map<String, Integer> data) {
		this.data = data;
	}

}
