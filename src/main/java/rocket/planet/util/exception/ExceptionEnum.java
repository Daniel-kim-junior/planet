package rocket.planet.util.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ExceptionEnum {

	SECURITY_AUTH_EXCEPTION(HttpStatus.UNAUTHORIZED, "SE001", "등록되지 않은 사용자입니다"),

	SECURITY_ACCESS_EXCEPTION(HttpStatus.UNAUTHORIZED, "SE002", "권한이 없는 사용자입니다"),

	SECURITY_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "ST001", "토큰이 유효하지 않습니다"),

	UNKNOWN_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "CE001", "서버에 문제가 발생했습니다"),

	USER_ID_NOT_FOUND_EXCEPTION("UE001", "해당 아이디가 존재하지 않습니다"),

	USER_PASSWORD_NOT_MATCH_EXCEPTION("UE002", "비밀번호가 일치하지 않습니다"),

	EMAIL_NOT_VALID_EXCEPTION("UE003", "이메일 형식이 올바르지 않습니다"),

	PASSWORD_NOT_VALID_EXCEPTION("UE004", "8자 이상 16자 미만의 특수문자, 숫자, 영문자 조합이어야 합니다"),

	EMAIL_DUP_FOUND_EXCEPTION("UE005", "이미 존재하는 이메일 입니다"),

	REQUEST_NOT_VALID_EXCEPTION("UE006", "요청이 올바르지 않습니다"),

	EMAIL_TOKEN_NOT_FOUND_EXCEPTION("UE007", "이메일 토큰이 만료되었습니다"),

	EMAIL_TOKEN_NOT_VALID_EXCEPTION("UE008", "이메일 토큰이 유효하지 않습니다"),

	EMAIL_ALREADY_AUTHORIZED_EXCEPTION("UE009", "이메일이 이미 인증되었습니다"),

	TEMP_LOCK_EXCEPTION("UE010", "5회 비밀번호 실패 이유로 30분간 계정 잠금"),

	AUTH_CHANGE_EXCEPTION("UE011", "인증 변경 중입니다. 잠시 후 다시 시도해주세요"),

	PASSWORD_MATCH_EXCEPTION("UE012", "이전 비밀번호와 동일합니다"),

	ALREADY_EXISTS_DEPT_EXCEPTION("UE013", "이미 존재하는 부서입니다"),

	INVALID_JWT_EXCEPTION("UE014", "로그인이 만료되었습니다"),

	INVALID_ORG_TYPE_EXCEPTION("UE015", "DEVELOPMENT, NON_DEVELOPMENT, NONE 중 하나를 입력해주세요"),

	NO_ACCESS_AUTHORITY_EXCEPTION("AD001", "접근 권한이 없는 사용자입니다."),

	NO_USERNICKNAME_EXCEPTION("AD002", "해당하는 사용자는 존재하지 않습니다."),

	NO_AUTHORITY_EXCEPTION("AD003", "해당하는 권한이 없는 사용자입니다."),

	INVALID_EMAIL_REQ_TYPE_EXCEPTION("UE016", "find, join 중 하나를 입력해주세요"),

	MIN_NOT_UNIT_VALID_EXCEPTION("UE017", "유효하지 않은 숫자 단위 입니다");

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
