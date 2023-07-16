package rocket.planet.controller.auth;

import static rocket.planet.dto.auth.AuthDto.*;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.domain.User;
import rocket.planet.service.auth.AuthLoginAndJoinService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final String AUTHORIZATION_HEADER = "Authorization";

	private final AuthLoginAndJoinService authLoginAndJoinService;

	@PostMapping("/account-register")
	public ResponseEntity<LoginResDto> accountAdd(@Valid @RequestBody JoinReqDto dto) throws Exception {
		return ResponseEntity.ok().body(authLoginAndJoinService.checkJoin(dto));
	}

	@PostMapping("/basic-profile-register")
	public ResponseEntity<BasicInputResDto> basicProfileAdd(@Valid @RequestBody BasicInputReqDto dto,
		@AuthenticationPrincipal(expression = "user") User user) throws Exception {
		return ResponseEntity.ok().body(authLoginAndJoinService.saveBasicProfile(dto, user));
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResDto> login(@Valid @RequestBody LoginReqDto loginReqDto) throws Exception {
		return ResponseEntity.ok().body(authLoginAndJoinService.checkLogin(loginReqDto));
	}

	@PostMapping("/reissue")
	public ResponseEntity<LoginResDto> reissue(@RequestHeader(AUTHORIZATION_HEADER) String bearerToken) throws
		Exception {
		return ResponseEntity.ok().body(authLoginAndJoinService.makeReissue(bearerToken));
	}

}
