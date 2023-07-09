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
	public LoginResponseDto accountRegister(@Valid @RequestBody JoinReqDto dto) {
		return authLoginAndJoinService.authJoin(dto);
	}


	@PostMapping("/basic-profile-register")
	public String basicProfileRegister(@Valid @RequestBody BasicInputReqDto dto) {
		return authLoginAndJoinService.authBasicProfileAndAutoLogin(dto);
	}


	@PostMapping("/login")
	public LoginResponseDto login(@Valid @RequestBody LoginReqDto loginReqDto) {
		return authLoginAndJoinService.authLogin(loginReqDto);
	}

	@PostMapping("/reissue")
	public LoginResponseDto reissue(@Valid @RequestHeader(AUTHORIZATION_HEADER) String bearerToken) {
		return authLoginAndJoinService.reissue(bearerToken);
	}

}
