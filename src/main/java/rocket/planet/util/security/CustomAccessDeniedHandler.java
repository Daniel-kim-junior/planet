package rocket.planet.util.security;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import rocket.planet.dto.common.CommonErrorDto;
import rocket.planet.util.exception.ExceptionEnum;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private static ExceptionEnum exceptionEnum = ExceptionEnum.SECURITY_ACCESS_EXCEPTION;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException, ServletException {
		log.error("AccessDenied!!! message : " + accessDeniedException.getMessage());
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
