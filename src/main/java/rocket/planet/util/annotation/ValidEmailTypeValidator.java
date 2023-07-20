package rocket.planet.util.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidEmailTypeValidator implements ConstraintValidator<ValidEmailType, String> {
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		if (value.equals("join")) {
			return true;
		}

		if (value.equals("find")) {
			return true;
		}

		return false;
	}
}
