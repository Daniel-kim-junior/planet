package rocket.planet.util.aop;

import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/*
 * 로깅 처리를 위한 어드바이스(AOP)
 */
@Aspect
@Component
public class LoggingAdvice {
	private static final Logger logger = LoggerFactory.getLogger(LoggingAdvice.class);

	private String paramMapToString(Map<String, String[]> paramMap) {
		StringJoiner joiner = new StringJoiner(", ");
		return paramMap.entrySet().stream()
			.map(entry -> String.format("%s -> (%s)", entry.getKey(), String.join(",", entry.getValue())))
			.collect(Collectors.joining(", "));
	}

	@Pointcut("within(rocket.planet.controller..*)")
	public void onRequest() {
	}

	@Around("rocket.planet.util.aop.LoggingAdvice.onRequest()")
	public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {
		HttpServletRequest request =
			((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
				.getRequest();
		Map<String, String[]> paramMap = request.getParameterMap();
		String params = "";
		if (paramMap.isEmpty() == false) {
			params = " [" + paramMapToString(paramMap) + "]";
		}
		long start = System.currentTimeMillis();
		try {
			return pjp.proceed(pjp.getArgs());
		} finally {
			long end = System.currentTimeMillis();
			System.out.println("호출");
			logger.debug("Request: {} {}{} < {} ({}ms)", request.getMethod(), request.getRequestURI(),
				params, request.getRemoteHost(), end - start);
		}
	}
}
