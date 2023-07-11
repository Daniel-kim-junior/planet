package rocket.planet.util.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import rocket.planet.repository.redis.AuthChangeRepository;

@Component
@Aspect
@RequiredArgsConstructor
public class AuthChangeAdvice {

	private final AuthChangeRepository authChangeRepository;

	@Pointcut("within(rocket.planet.service..*)")
	public void onService() {
	}

	@Before("onService()")
	public void onAuthChange(JoinPoint joinPoint) throws Throwable {
		System.out.println(joinPoint);
	}

}
