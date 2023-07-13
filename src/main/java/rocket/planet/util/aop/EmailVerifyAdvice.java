package rocket.planet.util.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import rocket.planet.dto.email.EmailDto.EmailDuplicateCheckAndSendEmailReqDto;
import rocket.planet.repository.redis.EmailFindConfirmRepository;
import rocket.planet.repository.redis.EmailJoinConfirmRepository;
import rocket.planet.util.exception.IdVerifiedException;

@Aspect
@Component
@RequiredArgsConstructor
public class EmailVerifyAdvice {

	private final EmailJoinConfirmRepository emailJoinConfirmRepository;

	private final EmailFindConfirmRepository emailFindConfirmRepository;

	@Pointcut("execution(* rocket.planet.controller.email.EmailVerifyController.emailVerify(..))")
	public void onRequest() {
	}

	@Before("onRequest()")
	public void beforeRequest(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		for (Object arg : args) {
			if (arg instanceof EmailDuplicateCheckAndSendEmailReqDto) {
				String id = ((EmailDuplicateCheckAndSendEmailReqDto)arg).getId();
				if (((EmailDuplicateCheckAndSendEmailReqDto)arg).getType().equals("find")) {
					if (emailFindConfirmRepository.findById(id).isPresent())
						throw new IdVerifiedException();
				} else if (((EmailDuplicateCheckAndSendEmailReqDto)arg).getType().equals("join")) {
					if (emailJoinConfirmRepository.findById(id).isPresent())
						throw new IdVerifiedException();
				}
			}
		}
	}
}
