package rocket.planet.dto.auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import rocket.planet.util.annotation.ValidPassword;

@Getter
@Builder
@ToString
public class PasswordModifyReqDto {

	@NotEmpty
	@NotBlank
	String id;

	@ValidPassword
	String password;

}
