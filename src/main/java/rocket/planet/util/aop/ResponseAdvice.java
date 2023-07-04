package rocket.planet.util.aop;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import rocket.planet.dto.common.CommonErrorDto;
import rocket.planet.util.common.ResponseUtil;

/*
 * Response 공통 처리를 위한 어드바이스(AOP)
 */
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
		Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
		ServerHttpResponse response) {

		if (body instanceof CommonErrorDto) {
			return ResponseUtil.error((CommonErrorDto)body);
		}
		return ResponseUtil.success(body);
	}
}
