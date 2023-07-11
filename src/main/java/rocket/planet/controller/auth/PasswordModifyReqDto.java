package rocket.planet.controller.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordModifyReqDto {

	String id;

	String password;
}
