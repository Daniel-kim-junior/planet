package rocket.planet.util.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import rocket.planet.repository.redis.AuthChangeRepository;

@Component
@Aspect
@RequiredArgsConstructor
public class AuthChangeAdvice {

	private final AuthChangeRepository authChangeRepository;

	// @Pointcut("execution(* rocket.planet.service(..))")
	// private void onService() {}

	// @Pointcut("within(rocket.planet.service..*)")
	// public void onService() {}
	//
	// @Around("onService()")
	// public void onAuthChange(ProceedingJoinPoint joinPoint) throws Throwable {
	//
	// }

}
