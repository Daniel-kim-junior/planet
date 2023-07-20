package rocket.planet.util.annotation;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Constraint(validatedBy = ValidDeptValidator.class)
@Target(ElementType.FIELD)
public @interface ValidDept {
	String message() default "DEVELOPMENT, NON_DEVELOPMENT, NONE";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
