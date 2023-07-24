package rocket.planet.dto.search;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SearchDto {

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class SearchReqDto {
		private String keyword;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class SearchResDto {
		private String userNickName;
		private SearchOrgResDto userOrg;
		private List<SearchTechResDto> userTech;
		private List<SearchUserPjtStatusResDto> userStatus;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class SearchTechResDto {
		private String techName;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class SearchOrgResDto {
		private String deptName;
		private String teamName;
		private String userTeamType;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class SearchUserPjtStatusResDto {
		private String projectName;
		private LocalDate userPjtCloseDt;
	}
}
