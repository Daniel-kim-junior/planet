package rocket.planet.util.annotation;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ValidPasswordValidator.class)
@Target(ElementType.FIELD)
public @interface ValidPassword {
	String message() default "/^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^!@#$%^&*_-])[a-zA-Z\\d!@#$%^&*_\\-]{8,}$/;";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}