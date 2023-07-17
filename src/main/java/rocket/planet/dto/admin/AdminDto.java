package rocket.planet.dto.admin;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import rocket.planet.domain.AuthType;
import rocket.planet.dto.common.ListResDto;

public class AdminDto {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class AdminAuthModifyReqDto {
		private String userNickName;
		private String deptName;
		private String teamName;
		private String role;

		@Builder
		public AdminAuthModifyReqDto(String userNickName, String deptName, String teamName, String role) {
			this.userNickName = userNickName;
			this.deptName = deptName;
			this.teamName = teamName;
			this.role = role;
		}
	}

	@Getter
	@ToString
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class AdminAuthMemberResDto {
		private String userNickName;
		private String deptName;
		private String teamName;
		private String role;
		private LocalDate profileStartDt;
		private boolean isActive;

		@Builder
		public AdminAuthMemberResDto(String userNickName, String deptName, String teamName, String role,
			LocalDate profileStartDt, boolean isActive) {
			this.userNickName = userNickName;
			this.deptName = deptName;
			this.teamName = teamName;
			this.role = role;
			this.profileStartDt = profileStartDt;
			this.isActive = isActive;
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class AdminAddAuthDto {
		private UUID authTargetId;
		private AuthType authType;
		private String authorizerNickName;
		private String authNickName;

		@Builder
		public AdminAddAuthDto(UUID authTargetId, AuthType authType, String authorizerNickName, String authNickName) {
			this.authTargetId = authTargetId;
			this.authType = authType;
			this.authorizerNickName = authorizerNickName;
			this.authNickName = authNickName;
		}
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
