package rocket.planet.util.common;

import rocket.planet.dto.common.CommonErrorDto;
import rocket.planet.dto.common.CommonResponse;

/*
 * 공통 응답 객체 생성 유틸
 */
public class ResponseUtil {
	public static <T> CommonResponse<T> success(T response) {
		return new CommonResponse<>(true, response, null);
	}

	public static <T> CommonResponse<T> error(CommonErrorDto error) {
		return new CommonResponse<>(false, null, error);
	}
}
