package rocket.planet.service.email;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import rocket.planet.dto.common.CommonResponse;
import rocket.planet.dto.email.EmailToken;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.util.exception.NoSuchEmailException;
import rocket.planet.util.exception.NoValidEmailTokenException;

/*
 * 이메일 인증 서비스
 * 1. 이메일 인증번호를 생성하고, 이메일로 전송한다.
 * 2. 이메일 인증번호를 redis에 저장한다.
 * 3. 이메일 인증번호를 입력받아 redis에 저장된 인증번호와 일치하는지 확인한다.
 * 4. 인증번호가 일치하면 이메일 인증여부를 true로 변경한다.
 */
@Service
@RequiredArgsConstructor
public class EmailVerifyService {
	private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final String EMAIL_TITLE = "ROCKET PLANET 회원가입 인증번호입니다.";
	private static final Long EXPIRE_TIME = 3L;
	private final UserRepository userRepository;
	private final JavaMailSender mailSender;
	private String tokenToJson;
	private EmailToken token;
	private HashOperations<String, Object, Object> hashOperations;
	private final ObjectMapper objectMapper;
	private final RedisTemplate<String, String> redisTemplate;

	@Async
	public CompletableFuture<String> saveLimitTimeAndSendEmail(String email) {
		boolean existUser = userRepository.findAll()
			.stream()
			.anyMatch(user -> StringUtils.pathEquals(user.getUserId(), email));
		if (!existUser && !email.equals("test20412041@gmail.com")) {
			CompletableFuture<String> future = new CompletableFuture<>();
			future.completeExceptionally(new NoSuchEmailException());
			return future;
		}
		String generateRandomString = generateRandomString(6);
		sendMail(email, generateRandomString);

		CompletableFuture<String> future = new CompletableFuture<>();
		future.complete(generateRandomString);
		return future;
	}

	public CommonResponse<String> redisSaveToken(String email, String generateRandomString) throws
		JsonProcessingException {
		redisTemplate.expire("emailToken:" + email, EXPIRE_TIME, TimeUnit.MINUTES);
		hashOperations = redisTemplate.opsForHash();
		token = EmailToken.builder()
			.isVerified(false)
			.id(email)
			.randomString(generateRandomString)
			.build();
		// TODO: send email
		tokenToJson = objectMapper.writeValueAsString(token);
		hashOperations.put("emailToken:" + email, email, tokenToJson);
		return new CommonResponse<>(true, "email 인증번호를 보냈습니다", null);
	}

	public void sendMail(String email, String sendEmailToken) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject(EMAIL_TITLE);
		message.setText(sendEmailToken);
		mailSender.send(message);
	}

	public CommonResponse<String> confirmByRedisEmailTokenAndSaveToken(String email, String reqToken) throws
		JsonProcessingException {
		redisTemplate.expire("emailToken:" + email, EXPIRE_TIME, TimeUnit.MINUTES);
		hashOperations = redisTemplate.opsForHash();
		String tokenJson = (String)hashOperations.get("emailToken:" + email, email);
		token = objectMapper.readValue(tokenJson, EmailToken.class);
		if (token != null && token.getRandomString().equals(reqToken)) {
			tokenToJson = objectMapper.writeValueAsString(token.verifiedToken());
			hashOperations.put("emailToken:" + email, email, tokenToJson);
			return new CommonResponse<>(true, "email 인증이 완료되었습니다", null);
		}
		throw new NoValidEmailTokenException();
	}

	public String generateRandomString(int length) {
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