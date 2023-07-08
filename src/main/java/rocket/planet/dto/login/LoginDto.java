package rocket.planet.dto.login;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 * 로그인 요청 DTO
 */
public class LoginDto {
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LoginRequestDto {
		@Email
		@NotEmpty
		@NotBlank
		private String id;

		@NotEmpty
		@NotBlank
		private String password;

	}
}

