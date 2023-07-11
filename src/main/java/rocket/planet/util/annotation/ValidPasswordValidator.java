package rocket.planet.util.annotation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/*
 * 어노테이션 구현체
 */
public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String> {
	private static final String PASSWORD_REGEX = "^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,./?])(?=.*[A-Za-z])(?=.*\\d)(?!.*[<>]).{8,16}$";
	private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (PASSWORD_PATTERN.matcher(value).matches()) {
			return true;
		}
		return false;
	}
}