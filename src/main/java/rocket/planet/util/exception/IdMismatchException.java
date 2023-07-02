package rocket.planet.util.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/*
 * 아이디 불일치 예외(Spring Security Exception)
 */
public class IdMismatchException extends UsernameNotFoundException {
	public IdMismatchException(String msg) {
		super(msg);
	}
}
