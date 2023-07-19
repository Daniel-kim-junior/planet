package rocket.planet.util.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidDeptValidator implements ConstraintValidator<ValidDept, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value.equals("DEVELOPMENT")) {
			return true;
		}
		if (value.equals("NON_DEVELOPMENT")) {
			return true;
		}
		if (value.equals("NONE")) {
			return true;
		}
		return false;
	}
}
