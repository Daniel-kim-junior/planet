package rocket.planet.dto.login;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rocket.planet.util.annotation.ValidPassword;

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
		private String id;

		@NotEmpty
		@ValidPassword
		private String password;

	}
}

