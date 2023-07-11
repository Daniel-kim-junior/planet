package rocket.planet.util.aop;

import static rocket.planet.util.aop.ExceptionAdvice.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.lettuce.core.RedisException;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.dto.common.CommonErrorDto;
import rocket.planet.util.exception.ExceptionEnum;
import rocket.planet.util.exception.IdVerifiedException;

@ControllerAdvice
@Slf4j
public class AopExceptionAdvice {

	@ExceptionHandler(IdVerifiedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public CommonErrorDto handleRedisException(RedisException e) {
		log.error("이미 인증된 아이디입니다", e.getClass().getSimpleName(), e.getMessage());
		return getCommonErrorDto(ExceptionEnum.EMAIL_ALREADY_AUTHORIZED_EXCEPTION);
	}

}
