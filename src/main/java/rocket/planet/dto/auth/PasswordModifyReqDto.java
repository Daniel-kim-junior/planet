package rocket.planet.dto.auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import rocket.planet.util.annotation.ValidPassword;

@Getter
@NoArgsConstructor
public class PasswordModifyReqDto {

	@NotEmpty
	@NotBlank
	String id;

	@ValidPassword
	String password;

}
