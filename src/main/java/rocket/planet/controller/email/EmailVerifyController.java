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
	public ResponseEntity<String> emailVerify(
		@Valid @RequestBody EmailDuplicateCheckAndSendEmailReqDto dto) {
		String email = dto.getId();
		CompletableFuture<String> gen = emailVerifyService.saveLimitTimeAndSendEmail(email);
		try {
			emailVerifyService.saveRedisToken(email, gen.get().toString(), dto.getType());
			return ResponseEntity.ok().body("이메일 전송을 완료했습니다");
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			throw new NoSuchEmailException();
		}
	}

	@PostMapping(value = "/email-verify/check", headers = "Accept=application/json", produces = "application/json")
	public ResponseEntity<String> emailVerifyCheck(@Valid @RequestBody EmailVerifyCheckReqDto dto) throws
		RedisException {
		return ResponseEntity.ok()
			.body(emailVerifyService.checkByRedisEmailTokenAndSaveToken(dto.getId(), dto.getCode()));
	}

}
