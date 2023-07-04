package rocket.planet.controller.test;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import rocket.planet.dto.common.CommonResponse;
import rocket.planet.dto.login.LoginRequestDto;
import rocket.planet.util.exception.IdMismatchException;
import rocket.planet.util.exception.PasswordMismatchException;

/*
 * 테스트용 예외 컨트롤러
 */
@RestController
@RequestMapping("/test/error")
public class ExceptionController {

	@GetMapping
	public CommonResponse<String> getTest(@Parameter(description = "사용자 id") String userId) {
		CommonResponse<String> response = new CommonResponse<>(true, "Success", null);
		return response;
	}

	@PostMapping("/id-mismatch")
	public String idMismatch(@Valid @RequestBody LoginRequestDto loginRequest) {
		if (!loginRequest.getId().equals("testId")) {
			throw new IdMismatchException("id mismatch");
		}
		return "dd";
	}

	@PostMapping("/password-mismatch")
	public String passwordMismatch(@Valid @RequestBody LoginRequestDto loginRequest) {
		if (!loginRequest.getPassword().equals("testPassword")) {
			throw new PasswordMismatchException("password mismatch");
		}
		return "login success";
	}

	@PostMapping("/user-data-invalid")
	public String emailInvalid(@Valid @RequestBody LoginRequestDto loginRequest) {
		return "email valid";
	}

}
