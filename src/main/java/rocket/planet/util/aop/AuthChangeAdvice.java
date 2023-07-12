//package rocket.planet.util.aop;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
//import lombok.RequiredArgsConstructor;
//import rocket.planet.repository.redis.AuthChangeRepository;
//import rocket.planet.util.exception.AuthChangeException;
//import rocket.planet.util.security.UserDetailsImpl;
//
//@Component
//@Aspect
//@RequiredArgsConstructor
//public class AuthChangeAdvice {
//
//	private final AuthChangeRepository authChangeRepository;
//
//	@Pointcut("within(rocket.planet.service..*) && "
//		+ "!within(rocket.planet.service.auth..*) && "
//		+ "!within(rocket.planet.service.email..*)")
//	public void onService() {
//	}
//
//	@Before("onService()")
//	public void onAuthChange(JoinPoint joinPoint) throws Throwable {
//
//		String loginUserId = UserDetailsImpl.getLoginUserId();
//		if (loginUserId.equals("anonymousUser")) {
//			return;
//		}
//		if (authChangeRepository.findById(loginUserId).isPresent()) {
//			throw new AuthChangeException();
//		}
//	}
//
//}