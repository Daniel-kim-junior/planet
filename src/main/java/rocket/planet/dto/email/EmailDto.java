package rocket.planet.dto.email;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class EmailDto {

	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@Builder
	public static class EmailResDto {
		private String message;
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@Builder
	public static class SendMailDto {
		private String address;
		private String title;
		private String message;
	}
}
