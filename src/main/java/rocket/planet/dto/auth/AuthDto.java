package rocket.planet.dto.auth;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rocket.planet.dto.auth.AuthDto.LoginResDto.AuthOrg;

public class AuthDto {

	@Getter
	@NoArgsConstructor
	public static class JoinReqDto {
		@Email
		@NotEmpty
		@NotBlank
		private String id;

		@NotEmpty
		@NotBlank
		private String password;

	}

	@Getter
	@NoArgsConstructor
	public static class BasicInputReqDto {
		@Email
		@NotEmpty
		@NotBlank
		private String id;

		@NotEmpty
		@NotBlank
		private String userName;

		@NotEmpty
		@NotBlank
		private String deptName;

		@NotEmpty
		@NotBlank
		private String teamName;

		private int career;

		private LocalDate companyJoinDate;

		private LocalDate userBirth;

		private boolean profileDisplay;

	}

	@Getter
	@Builder
	public static class BasicInputResDto {
		private String authRole;

		private AuthOrg authOrg;

		private boolean isThreeMonth;

		private String userNickName;
	}

	@Getter
	@Builder
	public static class LoginResDto {
		private String authRole;

		private String grantType;

		private AuthOrg authOrg;

		private String accessToken;
		private String refreshToken;

		private boolean isThreeMonth;

		private String userNickName;

		@Builder
		@Getter
		public static class AuthOrg {
			private String companyName;

			private String deptName;

			private String teamName;
		}

	}

	@Getter
	@NoArgsConstructor
	public static class LoginReqDto {
		@Email
		@NotEmpty
		@NotBlank
		private String id;

		@NotEmpty
		@NotBlank
		private String password;
	}
}
