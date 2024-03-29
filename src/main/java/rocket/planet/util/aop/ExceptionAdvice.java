package rocket.planet.util.aop;

import java.lang.reflect.Field;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSendException;
import org.springframework.security.access.AccessDeniedException;
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
import rocket.planet.util.annotation.ValidDept;
import rocket.planet.util.annotation.ValidEmailType;
import rocket.planet.util.exception.AlreadyExistsDeptException;
import rocket.planet.util.exception.AlreadyExistsIdException;
import rocket.planet.util.exception.DuplicateException;
import rocket.planet.util.exception.ExceptionEnum;
import rocket.planet.util.exception.IdMismatchException;
import rocket.planet.util.exception.JwtInvalidException;
import rocket.planet.util.exception.NoAccessAuthorityException;
import rocket.planet.util.exception.NoAuthorityException;
import rocket.planet.util.exception.NoSuchEmailException;
import rocket.planet.util.exception.NoSuchEmailTokenException;
import rocket.planet.util.exception.NoUserNickNameException;
import rocket.planet.util.exception.NoValidEmailTokenException;
import rocket.planet.util.exception.PasswordMatchException;
import rocket.planet.util.exception.PasswordMismatchException;
import rocket.planet.util.exception.ReqNotFoundException;
import rocket.planet.util.exception.Temp30MinuteLockException;
import rocket.planet.util.exception.UserLogException;
import rocket.planet.util.exception.UserPwdCheckException;
import rocket.planet.util.exception.UserTechException;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

	public static CommonErrorDto getCommonErrorDto(ExceptionEnum exceptionEnum) {
		return CommonErrorDto.builder().code(exceptionEnum.getCode()).message(exceptionEnum.getMessage()).build();
	}

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

	@ExceptionHandler(JwtInvalidException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public CommonErrorDto jwtInvalidException(JwtInvalidException e) {
		log.error("INTERNAL JWT INVALID EXCEPTION", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.INVALID_JWT_EXCEPTION);
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public CommonErrorDto jwtInvalidException(AccessDeniedException e) {
		log.error("Access Denied", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.SECURITY_ACCESS_EXCEPTION);
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
			} else if (field != null && field.isAnnotationPresent(ValidDept.class)) {
				log.error("OrgTypeValidException", e.getClass().getSimpleName(), e.getMessage());
				return getCommonErrorDto(ExceptionEnum.INVALID_ORG_TYPE_EXCEPTION);
			} else if (field != null && field.isAnnotationPresent(ValidEmailType.class)) {
				log.error("InvalidEmailRequestType", e.getClass().getSimpleName(), e.getMessage());
				return getCommonErrorDto(ExceptionEnum.INVALID_EMAIL_REQ_TYPE_EXCEPTION);
			} else if (field != null && field.isAnnotationPresent(Min.class)) {
				log.error("MinValidException", e.getClass().getSimpleName(), e.getMessage());
				return getCommonErrorDto(ExceptionEnum.MIN_NOT_UNIT_VALID_EXCEPTION);
			} else if (field != null && field.isAnnotationPresent(NotEmpty.class)) {
				log.error("Request Not Valid Exception", e.getClass().getSimpleName(), e.getMessage());
				return getCommonErrorDto(ExceptionEnum.REQUEST_NOT_VALID_EXCEPTION);
			} else if (field != null && field.isAnnotationPresent(NotBlank.class)) {
				log.error("Request Not Valid Exception", e.getClass().getSimpleName(), e.getMessage());
				return getCommonErrorDto(ExceptionEnum.REQUEST_NOT_VALID_EXCEPTION);
			}
		}
		return getCommonErrorDto(ExceptionEnum.UNKNOWN_SERVER_EXCEPTION);
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

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleConstraintViolationException(ConstraintViolationException e) {
		log.error("Request Param Validation Exception", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.REQUEST_NOT_VALID_EXCEPTION);
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
		return getCommonErrorDto(ExceptionEnum.EMAIL_DUP_FOUND_EXCEPTION);
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

	@ExceptionHandler(NoSuchEmailException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleUserIdNotFoundException(NoSuchEmailException e) {
		log.error("NoSuchEmail", e.getClass().getSimpleName(), e.getMessage());
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

	@ExceptionHandler(ReqNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleReqNotFoundException(ReqNotFoundException e) {
		log.error("handleReqNotFoundException", e.getClass().getSimpleName(), e.getMessage());
		return CommonErrorDto.builder().message(e.getMessage()).build();
	}

	@ExceptionHandler(DuplicateException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleDuplicateException(DuplicateException e) {
		log.error("handleDuplicateException", e.getClass().getSimpleName(), e.getMessage());
		return CommonErrorDto.builder().message(e.getMessage()).build();
	}

	@ExceptionHandler(NoAccessAuthorityException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleNoAccessAuthorityException(NoAccessAuthorityException e) {
		log.error("NoAccessAuthorityException", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.NO_ACCESS_AUTHORITY_EXCEPTION);
	}

	@ExceptionHandler(NoUserNickNameException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleNoUserNickNameException(NoAccessAuthorityException e) {
		log.error("NoUserNickNameException", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.NO_USERNICKNAME_EXCEPTION);
	}

	@ExceptionHandler(NoAuthorityException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonErrorDto handleNoAuthorityException(NoAuthorityException e) {
		log.error("NoAuthorityException", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.NO_AUTHORITY_EXCEPTION);
	}
}
