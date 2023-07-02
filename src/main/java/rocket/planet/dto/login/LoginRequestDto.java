package rocket.planet.dto.login;

import static lombok.AccessLevel.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import rocket.planet.util.annotation.ValidPassword;

/*
 * 로그인 요청 DTO
 */
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@ToString
public class LoginRequestDto {
	@Email
	@NotEmpty
	private String id;

	@NotEmpty
	@ValidPassword
	private String password;

}
