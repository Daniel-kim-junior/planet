package rocket.planet.util.aop;

import java.lang.reflect.Field;

import javax.validation.constraints.Email;

import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSendException;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.lettuce.core.RedisException;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.dto.common.CommonErrorDto;
import rocket.planet.util.annotation.ValidPassword;
import rocket.planet.util.exception.*;

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
		return CommonErrorDto.builder().code("UE-002").message(e.getMessage()).build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleConstraintViolationException(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();
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
	public CommonErrorDto handleNoValidEmailTokenException(NoValidEmailTokenException e) {
		log.error("NoValidEmailTokenException", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.EMAIL_TOKEN_NOT_VALID_EXCEPTION);
	}

	@ExceptionHandler(PasswordMatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handlePasswordMatchException(PasswordMatchException e) {
		log.error("PasswordMatchException", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.PASSWORD_MATCH_EXCEPTION);
	}

	@ExceptionHandler(MailSendException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public CommonErrorDto handleMailSendException(MailSendException e) {
		log.error("MailSendException", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.EMAIL_NOT_VALID_EXCEPTION);
	}

	@ExceptionHandler(RedisException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public CommonErrorDto handleRedisException(RedisException e) {
		log.error("RedisException", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.UNKNOWN_SERVER_EXCEPTION);
	}

	@ExceptionHandler(Temp30MinuteLockException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleTemp30MinuteLockException(Temp30MinuteLockException e) {
		log.error("Temp30MinuteLockException", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.TEMP_LOCK_EXCEPTION);
	}

	@ExceptionHandler(AlreadyExistsIdException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleAlreadyExistsIdException(AlreadyExistsIdException e) {
		log.error("AlreadyExistsIdException", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.EMAIL_NOT_FOUND_EXCEPTION);
	}

	@ExceptionHandler({Exception.class, NullPointerException.class, IllegalArgumentException.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public CommonErrorDto handleException(Exception e) {
		log.error("Exception", e.getClass().getSimpleName(), e.getMessage());
		e.printStackTrace();
		return getCommonErrorDto(ExceptionEnum.UNKNOWN_SERVER_EXCEPTION);
	}

	@ExceptionHandler(AlreadyExistsDeptException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleAlreadyExistsDeptException(AlreadyExistsDeptException e) {
		log.error("AlreadyExistsDeptException", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.ALREADY_EXISTS_DEPT_EXCEPTION);
	}

	@ExceptionHandler(UserTechException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleUserTechException(UserTechException e) {
		log.error("UserTechException", e.getClass().getSimpleName(), e.getMessage());
		return CommonErrorDto.builder().message(e.getMessage()).build();
	}

	@ExceptionHandler(UserPwdCheckException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleUserPwdCheckException(UserPwdCheckException e) {
		log.error("handleUserPwdCheckException", e.getClass().getSimpleName(), e.getMessage());
		return CommonErrorDto.builder().message(e.getMessage()).build();
	}

	@ExceptionHandler(UserLogException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleUserLogException(UserLogException e) {
		log.error("handleUserLogException", e.getClass().getSimpleName(), e.getMessage());
		return CommonErrorDto.builder().message(e.getMessage()).build();
	}

	static CommonErrorDto getCommonErrorDto(ExceptionEnum exceptionEnum) {
		return CommonErrorDto.builder().code(exceptionEnum.getCode()).message(exceptionEnum.getMessage()).build();
	}

}
