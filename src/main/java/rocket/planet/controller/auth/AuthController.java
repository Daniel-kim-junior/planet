package rocket.planet.controller.auth;

import static rocket.planet.dto.auth.AuthDto.*;

import javax.validation.Valid;

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
	public LoginResDto accountAdd(@Valid @RequestBody JoinReqDto dto) {
		return authLoginAndJoinService.checkJoin(dto);
	}

	@PostMapping("/basic-profile-register")
	public BasicInputResDto basicProfileAdd(@Valid @RequestBody BasicInputReqDto dto) {
		return authLoginAndJoinService.saveBasicProfileAndAutoLogin(dto);
	}

	@PostMapping("/login")
	public LoginResDto login(@Valid @RequestBody LoginReqDto loginReqDto) {
		return authLoginAndJoinService.checkLogin(loginReqDto);
	}

	@PostMapping("/reissue")
	public LoginResDto reissue(@Valid @RequestHeader(AUTHORIZATION_HEADER) String bearerToken) {
		return authLoginAndJoinService.makeReissue(bearerToken);
	}

}
