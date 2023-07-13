package rocket.planet.dto.admin;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import rocket.planet.domain.AuthType;

public class AdminDto {

	@Getter
	@Builder
	public static class AdminAuthModifyReqDto {
		private String userNickName;
		private String deptName;
		private String teamName;
		private String role;

	}

	@Getter
	@Builder
	public static class AdminAuthMemberResDto {
		private String userNickName;
		private String deptName;
		private String teamName;
		private String role;
		private LocalDate profileStartDt;
		private boolean isActive;
	}

	@Getter
	@Builder
	public static class AdminAddAuthDto {
		private UUID authTargetId;
		private AuthType authType;
		private String authorizerNickName;
		private String authNickName;

	}
}
