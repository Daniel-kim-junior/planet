package rocket.planet.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/*
 * 공통 응답 제네릭 객체
 */
@Getter
@ToString
@AllArgsConstructor
public class CommonResponse<T> {
	private boolean success;
	private T response;
	private CommonErrorDto error;
}
