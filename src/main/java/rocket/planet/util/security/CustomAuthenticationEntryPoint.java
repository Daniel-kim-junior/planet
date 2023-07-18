package rocket.planet.util.security;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import rocket.planet.dto.common.CommonErrorDto;
import rocket.planet.util.exception.ExceptionEnum;

/*
 * 인증되지 않은 유저 접근 거부 예외 처리(Spring Security Exception Entry Point)
 */
@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static ExceptionEnum exceptionEnum = ExceptionEnum.SECURITY_AUTH_EXCEPTION;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		log.error("UnAuthorized!!! message : " + authException.getMessage());

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(exceptionEnum.getHttpStatus().value());
		try (OutputStream os = response.getOutputStream()) {
			ObjectMapper objectMapper = new ObjectMapper();
			CommonErrorDto error = CommonErrorDto.builder()
				.code(exceptionEnum.getCode())
				.message(exceptionEnum.getMessage())
				.build();
			objectMapper.writeValue(os, error);
			os.flush();
		}
	}
}
