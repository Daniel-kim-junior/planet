package rocket.planet.controller.profile;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.service.profile.ProfileService;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
	private final ProfileService profileService;

	@GetMapping("/{userNickName}")
	public String profileDetails(@PathVariable String userNickName) {
		profileService.getProfileDetailByUserNickName(userNickName);
		return userNickName + "님의 프로필입니다.";
	}

}
