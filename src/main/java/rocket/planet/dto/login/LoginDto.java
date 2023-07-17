package rocket.planet.dto.login;

import static lombok.AccessLevel.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 * 로그인 요청 DTO
 */
public class LoginDto {
	@Getter
	@NoArgsConstructor(access = PROTECTED)
	public static class LoginRequestDto {
		@Email
		@NotEmpty
		@NotBlank
		private String id;

		@NotEmpty
		@NotBlank
		private String password;

		@Builder
		public LoginRequestDto(String id, String password) {
			this.id = id;
			this.password = password;
		}
	}
}

