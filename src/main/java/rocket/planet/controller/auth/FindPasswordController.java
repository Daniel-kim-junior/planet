package rocket.planet.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.service.auth.AuthFindPasswordService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class FindPasswordController {

	private final AuthFindPasswordService authFindPasswordService;

	@PostMapping("/password/modify")
	public ResponseEntity<String> passwordModify(@RequestBody PasswordModifyReqDto dto) {
		return ResponseEntity.ok(authFindPasswordService.modifyPassword(dto));
	}
}
