package rocket.planet.service.email;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.domain.redis.EmailFindConfirm;
import rocket.planet.domain.redis.EmailFindToken;
import rocket.planet.domain.redis.EmailJoinConfirm;
import rocket.planet.domain.redis.EmailJoinToken;
import rocket.planet.domain.redis.EmailToken;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.repository.redis.EmailFindConfirmRepository;
import rocket.planet.repository.redis.EmailFindTokenRepository;
import rocket.planet.repository.redis.EmailJoinConfirmRepository;
import rocket.planet.repository.redis.EmailJoinTokenRepository;
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
@Slf4j
public class EmailVerifyService {
	private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final String EMAIL_TITLE = "ROCKET PLANET 회원가입 인증번호입니다.";

	private static final String EMAIL_CONFIRM_TITLE = "ROCKET PLANET 회원가입 인증이 완료되었습니다.";

	private static final String SEND_EMAIL_MESSAGE = "이메일 인증번호 전송을 완료했습니다";

	private static final Long EXPIRE_TIME = 3L;
	private final UserRepository userRepository;
	private final JavaMailSender mailSender;

	private final EmailJoinTokenRepository emailJoinTokenRepository;

	private final EmailJoinConfirmRepository emailJoinConfirmRepository;

	private final EmailFindTokenRepository emailFindTokenRepository;

	private final EmailFindConfirmRepository emailFindConfirmRepository;

	private EmailToken token;

	@Async("customAsyncExecutor")
	public CompletableFuture<String> saveLimitTimeAndSendEmail(String email, String type) {
		boolean existUser = userRepository.findByUserId(email).isPresent();
		if (existUser && StringUtils.pathEquals(type, "join")) {
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

	public String saveRedisToken(String email, String generatedRandomString, String type) throws RedisException {
		if (StringUtils.pathEquals(type, "join")) {
			token = EmailJoinToken.builder()
				.email(email)
				.token(generatedRandomString)
				.build();
			emailJoinTokenRepository.save((EmailJoinToken)token);
		} else if (StringUtils.pathEquals(type, "find")) {
			token = EmailFindToken.builder()
				.email(email)
				.token(generatedRandomString)
				.build();
			emailFindTokenRepository.save((EmailFindToken)token);
		}
		return SEND_EMAIL_MESSAGE;
	}

	public void sendMail(String email, String sendEmailToken) throws MailSendException {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject(EMAIL_TITLE);
		message.setText(sendEmailToken);
		mailSender.send(message);
	}

	@Transactional
	public String checkByRedisEmailTokenAndSaveToken(String email, String reqToken, String type) throws RedisException {

		if (StringUtils.pathEquals(type, "find")) {
			EmailFindToken emailFindToken = emailFindTokenRepository.findById(email)
				.orElseThrow(NoValidEmailTokenException::new);
			if (StringUtils.pathEquals(emailFindToken.getToken(), reqToken)) {
				emailFindConfirmRepository.save(EmailFindConfirm.builder()
					.email(email).build());
				emailFindTokenRepository.delete(emailFindToken);
			}
		} else if (StringUtils.pathEquals(type, "join")) {
			EmailJoinToken emailJoinToken = emailJoinTokenRepository.findById(email)
				.orElseThrow(NoValidEmailTokenException::new);
			if (StringUtils.pathEquals(token.getToken(), reqToken)) {
				emailJoinConfirmRepository.save(EmailJoinConfirm.builder()
					.email(email).build());
				emailJoinTokenRepository.delete(emailJoinToken);

			}
		}
		return EMAIL_CONFIRM_TITLE;
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