package rocket.planet.util.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

/*
 * Exception 정의 Enum
 */
@Getter
@ToString
public enum ExceptionEnum {

	// Security Exception
	SECURITY_AUTH_EXCEPTION(HttpStatus.UNAUTHORIZED, "SE001", "인증되지 않은 사용자입니다"),
	SECURITY_ACCESS_EXCEPTION(HttpStatus.UNAUTHORIZED, "SE002", "권한이 없는 사용자입니다"),

	// Common Exception
	UNKNOWN_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "CE001", "서버에 문제가 발생했습니다"),

	// Login Exception
	USER_ID_NOT_FOUND_EXCEPTION("UE001", "해당 아이디가 존재하지 않습니다"),
	USER_PASSWORD_NOT_MATCH_EXCEPTION("UE002", "비밀번호가 일치하지 않습니다"),

	// Invalid Exception
	EMAIL_NOT_VALID_EXCEPTION("UE003", "이메일 형식이 올바르지 않습니다"),

	PASSWORD_NOT_VALID_EXCEPTION("UE004", "8자 이상 16자 미만의 특수문자, 숫자, 영문자 조합이어야 합니다"),

	EMAIL_NOT_FOUND_EXCEPTION("UE005", "해당 이메일이 존재하지 않습니다"),

	// Request Exception
	REQUEST_NOT_VALID_EXCEPTION("UE006", "요청이 올바르지 않습니다"),
	EMAIL_TOKEN_NOT_FOUND_EXCEPTION("UE007", "이메일 토큰이 만료되었습니다"),
	EMAIL_TOKEN_NOT_VALID_EXCEPTION("UE008", "이메일 토큰이 유효하지 않습니다");

	private HttpStatus httpStatus;
	private final String code;
	private String message;

	ExceptionEnum(String code, String message) {
		this.message = message;
		this.code = code;
	}

	ExceptionEnum(HttpStatus httpStatus, String code, String message) {
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
	}

}