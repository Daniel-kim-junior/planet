package rocket.planet.controller.profile;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.domain.Profile;
import rocket.planet.domain.User;
import rocket.planet.dto.common.CommonResDto;
import rocket.planet.dto.profile.ProfileDto;
import rocket.planet.service.profile.ProfileService;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
	private final ProfileService profileService;

	@GetMapping("/{userNickName}")
	public ResponseEntity<ProfileDto.ProfileResDto> profileDetails(@PathVariable("userNickName") String userNickName) {
		ProfileDto.ProfileResDto profileDetail = profileService.getProfileDetailByUserNickName(userNickName);
		return ResponseEntity.ok().body(profileDetail);
	}

	@PatchMapping("/{userNickName}")
	public ResponseEntity<CommonResDto> profileModify(@RequestBody ProfileDto.ProfileUpDateResDto profileUpDateResDto,
		@AuthenticationPrincipal(expression = "profile") Profile profile) {
		String loginUser = profile.getUserNickName();
		return ResponseEntity.ok().body(profileService.modifyProfile(profileUpDateResDto, loginUser));
	}

	@PatchMapping("/display/{userNickName}")
	public ResponseEntity<CommonResDto> profileDisplayModify(
		@RequestBody ProfileDto.ProfileDisplayUpDateResDto displayUpDateResDto,
		@AuthenticationPrincipal(expression = "profile") Profile profile) {
		String loginUser = profile.getUserNickName();
		return ResponseEntity.ok().body(profileService.modifyProfileDisplay(displayUpDateResDto, loginUser));

	}

	@PatchMapping("/annual/{userNickName}")
	public ResponseEntity<CommonResDto> profileAnnualModify(
		@RequestBody ProfileDto.AnnualUpDateResDto annualUpDateResDto,
		@AuthenticationPrincipal(expression = "profile") Profile profile) {
		String loginUser = profile.getUserNickName();
		return ResponseEntity.ok().body(profileService.modifyAnnualStatus(annualUpDateResDto, loginUser));
	}

	@PostMapping("/outside")
	public ResponseEntity<CommonResDto> outsideProjectAdd(
		@RequestBody ProfileDto.OutsideProjectRegisterReqDto registerReqDto,
		@AuthenticationPrincipal(expression = "profile") Profile profile) {
		String loginUser = profile.getUserNickName();
		return ResponseEntity.ok().body(profileService.addOusideProject(registerReqDto, loginUser));
	}

	@PatchMapping("/outside")
	public ResponseEntity<CommonResDto> outsideProjectModify(
		@RequestBody ProfileDto.OutsideProjectUpdateReqDto updateReqDto,
		@AuthenticationPrincipal(expression = "profile") Profile profile) {
		String loginUser = profile.getUserNickName();
		return ResponseEntity.ok().body(profileService.modifyOusideProject(updateReqDto, loginUser));
	}

	@DeleteMapping("/outside")
	public ResponseEntity<CommonResDto> outsideProjectRemove(@RequestParam("pjtUid") String pjtUidString,
		@AuthenticationPrincipal(expression = "profile") Profile profile) {
		String loginUser = profile.getUserNickName();
		return ResponseEntity.ok().body(profileService.removeOutsideProject(pjtUidString, loginUser));
	}

	@PostMapping("/certs")
	public ResponseEntity<CommonResDto> certAdd(@RequestBody ProfileDto.CertRegisterResDto certRegisterResDto,
		@AuthenticationPrincipal(expression = "profile") Profile profile) {
		String loginUser = profile.getUserNickName();
		return ResponseEntity.ok().body(profileService.addCertification(certRegisterResDto, loginUser));
	}

	@DeleteMapping("/certs")
	public ResponseEntity<CommonResDto> userCertRemove(@RequestParam("certUid") String cetUidString,
		@AuthenticationPrincipal(expression = "profile") Profile profile) {
		String loginUser = profile.getUserNickName();
		return ResponseEntity.ok().body(profileService.removeCertification(cetUidString, loginUser));
	}

	@PostMapping("/tech")
	public ResponseEntity<CommonResDto> userProfileTechAdd(@RequestBody ProfileDto.TechRegisterReqDto techReqDto,
		@AuthenticationPrincipal(expression = "profile") Profile profile) {
		String loginUser = profile.getUserNickName();
		return ResponseEntity.ok().body(profileService.addUserTech(techReqDto, loginUser));
	}

	@DeleteMapping("/tech")
	public ResponseEntity<CommonResDto> userProfileTechRemove(@RequestParam("userTechId") String userTechIdString,
		@AuthenticationPrincipal(expression = "profile") Profile profile) {
		String loginUser = profile.getUserNickName();
		return ResponseEntity.ok().body(profileService.removeUserTech(userTechIdString, loginUser));
	}

	@PatchMapping("/inside")
	public ResponseEntity<CommonResDto> userInsidePjtModify(
		@RequestBody ProfileDto.insideProjectUpdateReqDto insidePjtReqDto,
		@AuthenticationPrincipal(expression = "profile") Profile profile) {
		String loginUser = profile.getUserNickName();
		return ResponseEntity.ok().body(profileService.modifyUserInsideProject(insidePjtReqDto, loginUser));
	}

	@PatchMapping("/modify-pwd")
	public ResponseEntity<CommonResDto> userPwdModify(@RequestBody ProfileDto.UserNewPwdReqDto newPwdReqDto,
		@AuthenticationPrincipal(expression = "user") User user) {
		String loginUser = user.getUserId();
		return ResponseEntity.ok().body(profileService.changeUserPwd(newPwdReqDto, loginUser));
	}

	@PostMapping("/visitor-log")
	public ResponseEntity<CommonResDto> visitorLogAdd(@RequestBody ProfileDto.VisitorReqDto visitorReqDto) {
		return ResponseEntity.ok().body(profileService.addProfileVisitor(visitorReqDto));
	}

}
