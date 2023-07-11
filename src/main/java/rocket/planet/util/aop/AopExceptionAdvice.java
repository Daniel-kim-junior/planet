package rocket.planet.util.aop;

import static rocket.planet.util.aop.ExceptionAdvice.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import rocket.planet.util.exception.ExceptionEnum;
import rocket.planet.util.exception.IdVerifiedException;

@ControllerAdvice
@Slf4j
public class AopExceptionAdvice {

	@ExceptionHandler(IdVerifiedException.class)
	@ResponseBody
	public ResponseEntity handleRedisException(IdVerifiedException e) {
		log.error("이미 인증된 아이디입니다", e.getClass().getSimpleName(), e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(getCommonErrorDto(ExceptionEnum.EMAIL_ALREADY_AUTHORIZED_EXCEPTION));
	}

}
