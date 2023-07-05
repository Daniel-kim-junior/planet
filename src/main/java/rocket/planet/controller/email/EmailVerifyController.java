package rocket.planet.controller.email;

import static rocket.planet.dto.email.EmailDto.*;

import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.dto.common.CommonResponse;
import rocket.planet.service.email.EmailVerifyService;
import rocket.planet.util.exception.NoSuchEmailException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmailVerifyController {

	private final EmailVerifyService emailVerifyService;

	@GetMapping(value = "/email-verify", headers = "Accept=application/json", produces = "application/json")
	public CompletableFuture<CommonResponse<String>> emailVerify(@RequestParam String email) {
		return emailVerifyService.saveLimitTimeAndSendEmail(email).exceptionally(throwable -> {
			throw new NoSuchEmailException();
		});
	}

	@GetMapping(value = "/email-verify/check", headers = "Accept=application/json", produces = "application/json")
	public EmailResDto emailVerifyCheck(@RequestParam String email, @RequestParam String code) {
		emailVerifyService.confirmByRedisEmailTokenAndSaveToken(email, code);
		return EmailResDto.builder().message("인증이 완료되었습니다").build();
	}

}
