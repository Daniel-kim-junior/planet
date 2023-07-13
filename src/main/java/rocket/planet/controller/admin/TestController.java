package rocket.planet.controller.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.service.auth.TestService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {
	private final String AUTHORIZATION_HEADER = "Authorization";

	private final TestService testService;

	@GetMapping("/user/test")
	public String test() {
		testService.test();
		return "admin/test";
	}
}
