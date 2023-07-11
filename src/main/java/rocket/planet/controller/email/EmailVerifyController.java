package rocket.planet.controller.email;

import static rocket.planet.dto.email.EmailDto.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import rocket.planet.service.email.EmailVerifyService;
import rocket.planet.util.exception.NoSuchEmailException;

/*
 * 이메일 인증 컨트롤러
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class EmailVerifyController {

	private final EmailVerifyService emailVerifyService;

	@PostMapping(value = "/email-verify", headers = "Accept=application/json", produces = "application/json")
	public CompletableFuture<String> emailVerify(
		@Valid @RequestBody EmailDuplicateCheckAndSendEmailReqDto dto) {
		String email = dto.getId();
		CompletableFuture<String> gen = emailVerifyService.saveLimitTimeAndSendEmail(email)
			.exceptionally(throwable -> {
				throw new NoSuchEmailException();
			});
		try {
			emailVerifyService.saveRedisToken(email, gen.get().toString());
			return CompletableFuture.completedFuture("이메일 전송을 완료했습니다");
		} catch (InterruptedException | ExecutionException | JsonProcessingException e) {
			throw new RedisException("Redis 서버 오류입니다");
		}
	}

	@PostMapping(value = "/email-verify/check", headers = "Accept=application/json", produces = "application/json")
	public String emailVerifyCheck(@Valid @RequestBody EmailVerifyCheckReqDto dto) {
		return emailVerifyService.checkByRedisEmailTokenAndSaveToken(dto.getId(), dto.getCode());
	}

}
