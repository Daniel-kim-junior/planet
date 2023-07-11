package rocket.planet.dto.email;

import static lombok.AccessLevel.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class EmailDto {
	@Getter
	@NoArgsConstructor(access = PROTECTED)
	public static class SendMailReqDto {
		private String address;
		private String title;
		private String message;
	}

	@Getter
	@NoArgsConstructor(access = PROTECTED)
	public static class EmailDuplicateCheckAndSendEmailReqDto {
		@Email
		@NotBlank
		@NotEmpty
		private String id;
	}

	@Getter
	@NoArgsConstructor(access = PROTECTED)
	public static class EmailVerifyCheckReqDto {
		@Email
		@NotBlank
		@NotEmpty
		private String id;

		@NotBlank
		@NotEmpty
		private String code;
	}
}
