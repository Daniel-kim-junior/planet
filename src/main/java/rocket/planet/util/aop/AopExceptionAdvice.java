package rocket.planet.util.aop;

import static rocket.planet.util.aop.ExceptionAdvice.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import rocket.planet.util.exception.AuthChangeException;
import rocket.planet.util.exception.ExceptionEnum;
import rocket.planet.util.exception.IdVerifiedException;

@ControllerAdvice
@Slf4j
public class AopExceptionAdvice {

	@ExceptionHandler(IdVerifiedException.class)
	public ResponseEntity handleRedisException(IdVerifiedException e) {
		log.error("이미 인증된 아이디입니다", e.getClass().getSimpleName(), e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(getCommonErrorDto(ExceptionEnum.EMAIL_ALREADY_AUTHORIZED_EXCEPTION));
	}

	@ExceptionHandler(AuthChangeException.class)
	public ResponseEntity handleRedisException(AuthChangeException e) {
		log.error("인증 변경 중입니다. 잠시 후 다시 시도해주세요", e.getClass().getSimpleName(), e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(getCommonErrorDto(ExceptionEnum.AUTH_CHANGE_EXCEPTION));
	}

}
