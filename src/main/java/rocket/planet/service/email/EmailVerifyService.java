package rocket.planet.service.email;

import static rocket.planet.dto.email.EmailDto.*;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import rocket.planet.dto.common.CommonResponse;
import rocket.planet.dto.email.EmailToken;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.repository.redis.EmailTokenRepository;
import rocket.planet.util.exception.NoSuchEmailException;
import rocket.planet.util.exception.NoValidEmailTokenException;

@Service
@RequiredArgsConstructor
public class EmailVerifyService {
	private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final String EMAIL_TITLE = "ROCKET PLANET 회원가입 인증번호입니다.";
	private final UserRepository userRepository;
	private final EmailTokenRepository emailTokenRepository;
	private final SendToGmailService sendToGmailService;
	private final RedisTemplate<String, String> redisTemplate;

	@Async
	public CompletableFuture<CommonResponse<String>> saveLimitTimeAndSendEmail(String email) {
		boolean existUser = userRepository.findAll()
			.stream()
			.anyMatch(user -> StringUtils.pathEquals(user.getUserId(), email));
		if (!existUser && !email.equals("test20412041@gmail.com")) {
			CompletableFuture<CommonResponse<String>> future = new CompletableFuture<>();
			future.completeExceptionally(new NoSuchEmailException());
			return future;
		}
		EmailToken emailToken = EmailToken.builder().id(email).timeToLive(5L).random(generateRandomString(6)).build();
		emailTokenRepository.save(emailToken);
		// TODO: send email
		sendToGmailService.sendMail(
			SendMailDto.builder().address(email).title(EMAIL_TITLE).message(emailToken.getRandom()).build());
		CompletableFuture<CommonResponse<String>> future = new CompletableFuture<>();
		future.complete(new CommonResponse<>(true, "email 인증번호를 보냈습니다", null));
		return future;
	}

	public CommonResponse<String> confirmByRedisEmailTokenAndSaveToken(String email, String token) {
		HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
		Optional<EmailToken> emailToken = Optional.of((EmailToken)hashOperations.get("email_token", email));
		if (validationToken(emailToken, token)) {
			emailTokenRepository.delete(emailToken.get());
			return new CommonResponse<>(true, "email 인증이 완료되었습니다", null);
		}
		throw new NoValidEmailTokenException();
	}

	private boolean validationToken(Optional<EmailToken> emailToken, String token) {
		return emailToken.isPresent() && emailToken.get().getRandom().equals(token);
	}

	private String generateRandomString(int length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(CHARACTERS.length());
			char randomChar = CHARACTERS.charAt(randomIndex);
			sb.append(randomChar);
		}

		return sb.toString();
	}
}
