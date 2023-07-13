package rocket.planet.controller.profile;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.dto.visitor.ProfileVisitorListResDto;
import rocket.planet.service.profile.ProfileVisitorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile/invite")
public class ProfileVisitorController {

	private final ProfileVisitorService profileVisitorService;

	/**
	 * 최근 15일간 방문자 조회
	 *
	 * @return {UserId, userRole}
	 */
	@GetMapping
	public ResponseEntity<List<ProfileVisitorListResDto>> visitantList() {
		return ResponseEntity.ok().body(profileVisitorService.getVisitorList());
	}
}
