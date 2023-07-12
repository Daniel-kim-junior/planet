package rocket.planet.controller.profile;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.service.profile.ProfileVisitantService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile/invite")
public class ProfileVisitantController {

	private final ProfileVisitantService profileVisitantService;

	/**
	 * 최근 15일간 방문자 조회
	 *
	 * @return {UserId, userRole}
	 */
	@GetMapping
	public ResponseEntity<VisitantListResDto> visitantList() {
		// return ResponseEntity.ok().body(profileVisitantService.getVisitantList());
		return null;
	}
}
