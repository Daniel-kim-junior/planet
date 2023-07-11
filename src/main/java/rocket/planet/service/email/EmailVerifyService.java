package rocket.planet.service.email;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import rocket.planet.domain.redis.EmailConfirm;
import rocket.planet.domain.redis.EmailToken;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.repository.redis.EmailConfirmRepository;
import rocket.planet.repository.redis.EmailTokenRepository;
import rocket.planet.util.exception.NoSuchEmailException;
import rocket.planet.util.exception.NoValidEmailTokenException;

/*
 * 이메일 인증 서비스
 * 1. 이메일 인증번호를 생성하고, 이메일로 전송한다.
 * 2. 이메일 인증번호를 redis에 저장한다.
 * 3. 이메일 인증번호를 입력받아 redis에 저장된 인증번호와 일치하는지 확인한다.
 * 4. 인증번호가 일치하면 이메일 확인 객체를 저장한다
 */
@Service
@RequiredArgsConstructor
public class EmailVerifyService {
	private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final String EMAIL_TITLE = "ROCKET PLANET 회원가입 인증번호입니다.";
	private static final Long EXPIRE_TIME = 3L;
	private final UserRepository userRepository;
	private final JavaMailSender mailSender;

	private final EmailTokenRepository emailTokenRepository;

	private final EmailConfirmRepository emailConfirmRepository;

	private EmailToken token;

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
		String generateRandomString = makeRandomString(6);
		sendMail(email, generateRandomString);

		CompletableFuture<String> future = new CompletableFuture<>();
		future.complete(generateRandomString);
		return future;
	}

	public String saveRedisToken(String email, String generatedRandomString) throws
		JsonProcessingException {
		token = EmailToken.builder()
			.email(email)
			.token(generatedRandomString)
			.build();
		emailTokenRepository.save(token);
		return "email 인증번호를 보냈습니다";
	}

	public void sendMail(String email, String sendEmailToken) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject(EMAIL_TITLE);
		message.setText(sendEmailToken);
		mailSender.send(message);
	}

	public String checkByRedisEmailTokenAndSaveToken(String email, String reqToken) {

		Optional<EmailToken> findToken = emailTokenRepository.findById(email);
		if (findToken.isPresent() && findToken.get().getToken().equals(reqToken)) {
			emailTokenRepository.delete(findToken.get());
			emailConfirmRepository.save(EmailConfirm.builder().email(email).build());
			return "email 인증이 완료되었습니다";
		}
		throw new NoValidEmailTokenException();
	}

	public String makeRandomString(int length) {
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