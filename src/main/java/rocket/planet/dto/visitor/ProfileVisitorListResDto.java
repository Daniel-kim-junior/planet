package rocket.planet.dto.visitor;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileVisitorListResDto {
	private String userId;

	private String userRole;
}
