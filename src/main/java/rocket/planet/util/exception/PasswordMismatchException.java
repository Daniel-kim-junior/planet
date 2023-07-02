package rocket.planet.util.exception;

import org.springframework.security.authentication.BadCredentialsException;

/*
 * 비밀번호 불일치 예외(Spring Security Exception)
 */
public class PasswordMismatchException extends BadCredentialsException {
	public PasswordMismatchException(String msg) {
		super(msg);
	}
}
