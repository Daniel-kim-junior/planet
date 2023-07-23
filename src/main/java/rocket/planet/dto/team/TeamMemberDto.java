package rocket.planet.dto.team;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import rocket.planet.dto.common.ListResDto;

public class TeamMemberDto {
	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class TeamMemberInfoDto {
		private String userNickName;
		private String profileEmail;
		private LocalDate profileStart;
		private int profileCareer;
		private boolean isActive;
		private String deptName;
		private String teamName;
	}

	@Getter
	@SuperBuilder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class TeamMemberListDto extends ListResDto {
		private List<TeamMemberInfoDto> memberList;
	}
}
