package rocket.planet.dto.email;

import javax.validation.constraints.Email;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EmailDto {
	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@Builder
	public static class SendMailDto {
		private String address;
		private String title;
		private String message;
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Builder
	public static class EmailDuplicateCheckAndSendEmailDto {
		@Email
		private String email;
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Builder
	public static class EmailVerifyCheckDto {
		@Email
		private String email;

		private String code;
	}
}
