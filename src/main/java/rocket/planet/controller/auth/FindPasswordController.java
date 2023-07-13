package rocket.planet.controller.auth;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.dto.auth.PasswordModifyReqDto;
import rocket.planet.service.auth.AuthFindPasswordService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class FindPasswordController {

	private final AuthFindPasswordService authFindPasswordService;

	@PatchMapping("/password/modify")
	public ResponseEntity<String> passwordModify(@RequestBody @Valid PasswordModifyReqDto dto) throws Exception {
		return ResponseEntity.ok().body(authFindPasswordService.modifyPassword(dto));
	}

}
