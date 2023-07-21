package rocket.planet.dto.auth;

import static lombok.AccessLevel.*;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import rocket.planet.dto.auth.AuthDto.LoginResDto.AuthOrg;
import rocket.planet.util.annotation.ValidPassword;

public class AuthDto {

	@Getter
	@NoArgsConstructor(access = PROTECTED)
	public static class JoinReqDto {
		@Email
		@NotEmpty
		@NotBlank
		private String id;

		@NotEmpty
		@NotBlank
		@ValidPassword
		private String password;

		@Builder
		public JoinReqDto(String id, String password) {
			this.id = id;
			this.password = password;
		}
	}

	@Getter
	@NoArgsConstructor(access = PROTECTED)
	public static class BasicInputReqDto {

		@NotEmpty
		@NotBlank
		private String userName;

		@NotEmpty
		@NotBlank
		private String deptName;

		@NotEmpty
		@NotBlank
		private String teamName;

		@Min(0)
		private int career;

		private LocalDate companyJoinDate;

		private LocalDate userBirth;

		private boolean profileDisplay;

		@Builder
		public BasicInputReqDto(String userName, String deptName, String teamName, int career,
			LocalDate companyJoinDate,
			LocalDate userBirth, boolean profileDisplay) {
			this.userName = userName;
			this.deptName = deptName;
			this.teamName = teamName;
			this.career = career;
			this.companyJoinDate = companyJoinDate;
			this.userBirth = userBirth;
			this.profileDisplay = profileDisplay;
		}
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
	@NoArgsConstructor(access = PROTECTED)
	public static class LoginReqDto {
		@Email
		@NotEmpty
		@NotBlank
		private String id;

		@NotEmpty
		@NotBlank
		private String password;

		@Builder
		public LoginReqDto(String id, String password) {
			this.id = id;
			this.password = password;
		}
	}

	@NoArgsConstructor(access = PROTECTED)
	public static class LogOutResDto {

		private String message;

		@Builder
		public LogOutResDto(String message) {
			this.message = message;
		}
	}

	@NoArgsConstructor(access = PROTECTED)
	public static class PasswordModifyResDto {

		private String message;

		@Builder
		public PasswordModifyResDto(String message) {
			this.message = message;
		}

	}

	@Getter
	@ToString
	@NoArgsConstructor(access = PROTECTED)
	public static class PasswordModifyReqDto {

		@Email
		@NotEmpty
		@NotBlank
		String id;

		@NotBlank
		@NotEmpty
		@ValidPassword
		String password;

		@Builder
		public PasswordModifyReqDto(String id, String password) {
			this.id = id;
			this.password = password;
		}
	}

}
