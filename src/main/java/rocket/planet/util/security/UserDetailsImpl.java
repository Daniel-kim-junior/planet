package rocket.planet.util.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import rocket.planet.util.exception.IdVerifiedException;

public class UserDetailsImpl {

	public static String getLoginUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (String)Optional.ofNullable(authentication.getPrincipal()).orElseThrow(IdVerifiedException::new);
	}
}
