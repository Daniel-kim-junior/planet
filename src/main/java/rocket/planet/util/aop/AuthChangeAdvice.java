package rocket.planet.util.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import rocket.planet.repository.redis.AuthChangeRepository;
import rocket.planet.util.security.LoggedInUser;

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
