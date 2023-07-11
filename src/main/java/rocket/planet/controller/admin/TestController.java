package rocket.planet.controller.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {
	private final String AUTHORIZATION_HEADER = "Authorization";

	@GetMapping("/admin/test")
	public String test(@RequestHeader(AUTHORIZATION_HEADER) String token) {

		return "admin/test";
	}
}
