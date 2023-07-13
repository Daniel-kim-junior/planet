package rocket.planet.dto.team;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TeamMemberResDto {
	private String userNickName;
	private String profileEmail;
	private LocalDate profileStart;
	private int profileCareer;
	private boolean isActive;
}
