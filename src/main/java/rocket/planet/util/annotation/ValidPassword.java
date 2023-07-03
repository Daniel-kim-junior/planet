package rocket.planet.util.annotation;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/*
 * 유효한 비밀번호를 검증하는 어노테이션
 */
@Retention(RUNTIME)
@Constraint(validatedBy = ValidPasswordValidator.class)
@Target(ElementType.FIELD)
public @interface ValidPassword {
	String message() default "특수문자 포함 8~16자리로 숫자와 문자를 반드시 포함해서 입력해주세요.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
