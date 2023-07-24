package rocket.planet.util.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class IdMismatchException extends UsernameNotFoundException {
	public IdMismatchException(String msg) {
		super(msg);
	}
}
