package rocket.planet.controller.email;

import static rocket.planet.dto.email.EmailDto.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import rocket.planet.service.email.EmailVerifyService;

/*
 * 이메일 인증 컨트롤러
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class EmailVerifyController {

	private final EmailVerifyService emailVerifyService;

	@PostMapping(value = "/email-verify", headers = "Accept=application/json", produces = "application/json")
	public ResponseEntity<EmailVerifyResDto> emailVerify(
		@Valid @RequestBody EmailDuplicateCheckAndSendEmailReqDto dto) {
		final CompletableFuture<String> gen = emailVerifyService.saveLimitTimeAndSendEmail(dto);
		try {
			return ResponseEntity.ok().body(emailVerifyService.saveRedisToken(dto.getId(), gen.get(), dto.getType()));
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}

	@PostMapping(value = "/email-verify/check", headers = "Accept=application/json", produces = "application/json")
	public ResponseEntity<EmailVerifyCheckResDto> emailVerifyCheck(
		@Valid @RequestBody EmailVerifyCheckReqDto dto) throws
		RedisException {
		return ResponseEntity.ok()
			.body(emailVerifyService.checkByRedisEmailTokenAndSaveToken(dto.getId(), dto.getCode(), dto.getType()));
	}

}
