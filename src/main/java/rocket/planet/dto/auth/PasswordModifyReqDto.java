package rocket.planet.dto.auth;

import static lombok.AccessLevel.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import rocket.planet.util.annotation.ValidPassword;

@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
public class PasswordModifyReqDto {

	@NotEmpty
	@NotBlank
	String id;

	@ValidPassword
	String password;

	@Builder
	public PasswordModifyReqDto(String id, String password) {
		this.id = id;
		this.password = password;
	}
}
