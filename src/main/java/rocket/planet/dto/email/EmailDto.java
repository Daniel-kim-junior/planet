package rocket.planet.dto.email;

import static lombok.AccessLevel.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EmailDto {
	@Getter
	@NoArgsConstructor(access = PROTECTED)
	public static class SendMailReqDto {
		private String address;
		private String title;
		private String message;

		@Builder
		public SendMailReqDto(String address, String title, String message) {
			this.address = address;
			this.title = title;
			this.message = message;
		}
	}

	@Getter
	@NoArgsConstructor(access = PROTECTED)
	public static class EmailDuplicateCheckAndSendEmailReqDto {
		@Email
		@NotBlank
		@NotEmpty
		private String id;

		@NotBlank
		@NotEmpty
		private String type;

		@Builder
		public EmailDuplicateCheckAndSendEmailReqDto(String id, String type) {
			this.id = id;
			this.type = type;
		}
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

		@NotBlank
		@NotEmpty
		private String type;

		@Builder
		public EmailVerifyCheckReqDto(String id, String code, String type) {
			this.id = id;
			this.code = code;
			this.type = type;
		}
	}

	@Getter
	@NoArgsConstructor(access = PROTECTED)
	public static class EmailVerifyResDto {

		private String message;

		@Builder
		public EmailVerifyResDto(String message) {
			this.message = message;
		}

	}

	@Getter
	@NoArgsConstructor(access = PROTECTED)
	public static class EmailVerifyCheckResDto {

		private String message;

		@Builder
		public EmailVerifyCheckResDto(String message) {
			this.message = message;
		}

	}
}
