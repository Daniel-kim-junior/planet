package rocket.planet.dto.admin;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import rocket.planet.domain.AuthType;
import rocket.planet.dto.common.ListResDto;

public class AdminDto {

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class AdminAuthModifyReqDto {
		private String userNickName;
		private String deptName;
		private String teamName;
		private String role;

	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class AdminAuthMemberDto {
		private String userNickName;
		private String deptName;
		private String teamName;
		private String role;
		private LocalDate profileStartDt;
		private boolean isActive;

	}

	@Getter
	@SuperBuilder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class AdminAuthMemberListDto extends ListResDto {
		private List<AdminAuthMemberDto> adminAuthMemberList;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class AdminAddAuthDto {
		private UUID authTargetId;
		private AuthType authType;
		private String authorizerNickName;
		private String authNickName;

	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class AdminMemberOrgDto {
		private String userNickName;
		private String userEmail;
		private String deptName;
		private String teamName;
		private int userCareer;
		private LocalDate profileStartDt;
		private boolean isActive;
	}

	@Getter
	@SuperBuilder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class AdminMemberOrgListDto extends ListResDto {
		private List<AdminMemberOrgDto> memberList;
	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class AdminOrgModifyReqDto {
		private String userNickName;
		private String deptName;
		private String teamName;

	}
}
