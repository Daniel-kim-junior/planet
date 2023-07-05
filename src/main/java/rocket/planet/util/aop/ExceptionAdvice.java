package rocket.planet.util.aop;

import java.lang.reflect.Field;

import javax.validation.constraints.Email;

import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import rocket.planet.dto.common.CommonErrorDto;
import rocket.planet.util.annotation.ValidPassword;
import rocket.planet.util.exception.ExceptionEnum;
import rocket.planet.util.exception.IdMismatchException;
import rocket.planet.util.exception.NoSuchEmailException;
import rocket.planet.util.exception.NoSuchEmailTokenException;
import rocket.planet.util.exception.NoValidEmailTokenException;
import rocket.planet.util.exception.PasswordMismatchException;

/*
 * 예외 처리를 위한 어드바이스(AOP)
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler(IdMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleIdMismatchException(IdMismatchException e) {
		log.error("IdMismatchException({}) - {}", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.USER_ID_NOT_FOUND_EXCEPTION);
	}

	@ExceptionHandler(PasswordMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handlePasswordMismatchException(PasswordMismatchException e) {
		log.error("PasswordMismatchException", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.USER_PASSWORD_NOT_MATCH_EXCEPTION);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleConstraintViolationException(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();
		System.out.println(bindingResult);
		Object target = bindingResult.getTarget();
		Class<?> targetClass = target.getClass();
		Field field;
		String fieldName;
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			fieldName = fieldError.getField();
			field = ReflectionUtils.findField(targetClass, fieldName);
			if (field != null && field.isAnnotationPresent(Email.class)) {
				log.error("EmailValidException", e.getClass().getSimpleName(), e.getMessage());
				return getCommonErrorDto(ExceptionEnum.EMAIL_NOT_VALID_EXCEPTION);
			} else if (field != null && field.isAnnotationPresent(ValidPassword.class)) {
				log.error("PasswordValidException", e.getClass().getSimpleName(), e.getMessage());
				return getCommonErrorDto(ExceptionEnum.PASSWORD_NOT_VALID_EXCEPTION);
			}
		}
		return getCommonErrorDto(ExceptionEnum.UNKNOWN_SERVER_EXCEPTION);
	}

	@ExceptionHandler(NoSuchEmailException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleNoSuchEmailException(NoSuchEmailException e) {
		log.error("NoSuchEmailException", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.EMAIL_NOT_FOUND_EXCEPTION);
	}

	@ExceptionHandler(NoSuchEmailTokenException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleNoSuchEmailException(NoSuchEmailTokenException e) {
		log.error("NoSuchEmailTokenException", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.EMAIL_TOKEN_NOT_FOUND_EXCEPTION);
	}

	@ExceptionHandler(NoValidEmailTokenException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleNoValidEmailTokenException(NoSuchEmailTokenException e) {
		log.error("NoSuchEmailTokenException", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.EMAIL_TOKEN_NOT_VALID_EXCEPTION);
	}

	private CommonErrorDto getCommonErrorDto(ExceptionEnum exceptionEnum) {
		return CommonErrorDto.builder().code(exceptionEnum.getCode()).message(exceptionEnum.getMessage()).build();
	}

}
