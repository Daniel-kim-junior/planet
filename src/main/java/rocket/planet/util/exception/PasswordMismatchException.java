package rocket.planet.util.exception;

import org.springframework.security.authentication.BadCredentialsException;

public class PasswordMismatchException extends BadCredentialsException {
	public PasswordMismatchException(String msg) {
		super(msg);
	}
}
