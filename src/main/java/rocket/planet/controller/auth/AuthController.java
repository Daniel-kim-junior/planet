package rocket.planet.controller.auth;

import static rocket.planet.dto.auth.AuthDto.*;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.service.auth.AuthLoginAndJoinService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final String AUTHORIZATION_HEADER = "Authorization";

	private final AuthLoginAndJoinService authLoginAndJoinService;

	@PostMapping("/account-register")
	public ResponseEntity<LoginResDto> accountAdd(@Valid @RequestBody JoinReqDto dto) {
		return ResponseEntity.ok().body(authLoginAndJoinService.checkJoin(dto));
	}

	@PostMapping("/basic-profile-register")
	public ResponseEntity<BasicInputResDto> basicProfileAdd(@Valid @RequestBody BasicInputReqDto dto) {
		return ResponseEntity.ok().body(authLoginAndJoinService.saveBasicProfileAndAutoLogin(dto));
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResDto> login(@Valid @RequestBody LoginReqDto loginReqDto) {
		return ResponseEntity.ok().body(authLoginAndJoinService.checkLogin(loginReqDto));
	}

	@PostMapping("/reissue")
	public ResponseEntity<LoginResDto> reissue(@Valid @RequestHeader(AUTHORIZATION_HEADER) String bearerToken) {
		return ResponseEntity.ok().body(authLoginAndJoinService.makeReissue(bearerToken));
	}

}
