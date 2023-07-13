package rocket.planet.controller.profile;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.domain.ProfileTech;
import rocket.planet.repository.jpa.ProfileRepository;
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
    public ResponseEntity<String> profileModify(@RequestBody ProfileDto.ProfileUpDateResDto profileUpDateResDto) {
        log.info("updateResDto : {}", profileUpDateResDto);
        profileService.modifyProfile(profileUpDateResDto);
        return ResponseEntity.ok().body(profileUpDateResDto.getUserNickName() + "님의 프로필 수정이 완료되었습니다.");
    }
    @PatchMapping("/display/{userNickName}")
    public ResponseEntity<String> profileDisplayModify(@RequestBody ProfileDto.ProfileDisplayUpDateResDto displayUpDateResDto) {
        profileService.modifyProfileDisplay(displayUpDateResDto);
        if (displayUpDateResDto.isProfileDisplay()) {
            return ResponseEntity.ok().body(displayUpDateResDto.getUserNickName() + "님의 프로필이 공개로 변경되었습니다.");
        } else {
            return ResponseEntity.ok().body(displayUpDateResDto.getUserNickName() + "님의 프로필이 비공개로 변경되었습니다.");
        }
    }
    @PatchMapping("/annual/{userNickName}")
    public ResponseEntity<String> profileAnnualModify(@RequestBody ProfileDto.AnnualUpDateResDto annualUpDateResDto) {
        profileService.modifyAnnualStatus(annualUpDateResDto);
        if (annualUpDateResDto.isProfileAnnualStatus()) {
            return ResponseEntity.ok().body(annualUpDateResDto.getUserNickName() + "님의 상태가 휴가중으로 변경되었습니다.");
        } else {
            return ResponseEntity.ok().body(annualUpDateResDto.getUserNickName() + "님의 상태가 출근으로 변경되었습니다.");
        }
    }

    @PostMapping("/outside")
    public ResponseEntity<String> outsideProjectAdd(@RequestBody ProfileDto.OutsideProjectRegisterReqDto registerReqDto) {
        profileService.addOusideProject(registerReqDto);
        return ResponseEntity.ok().body("외부프로젝트 생성이 완료되었습니다.");
    }

    @PatchMapping("/outside")
    public ResponseEntity<String> outsideProjectModify(@RequestBody ProfileDto.OutsideProjectUpdateReqDto updateReqDto) {
        log.info("updateResDto : {}", updateReqDto);
        profileService.modifyOusideProject(updateReqDto);
        return ResponseEntity.ok().body("외부프로젝트 수정이 완료되었습니다.");
    }
    @DeleteMapping("/outside")
    public ResponseEntity<String> outsideProjectRemove(@RequestBody ProfileDto.OutsideProjectDeleteReqDto deleteReqDto) {
        profileService.removeOutsideProject(deleteReqDto);
        return ResponseEntity.ok().body(deleteReqDto.getPjtName() + "프로젝트를 삭제하였습니다.");
    }

    @PostMapping("/certs")
    public ResponseEntity<String> certAdd(@RequestBody ProfileDto.CertRegisterResDto certRegisterResDto) {
        profileService.addCertification(certRegisterResDto);
        return ResponseEntity.ok().body("자격증 생성이 완료되었습니다.");
    }
    @DeleteMapping("/certs")
    public ResponseEntity<String> certRemove(@RequestBody ProfileDto.CertDeleteReqDto certDeleteReqDto) {
        profileService.removeCertification(certDeleteReqDto);
        return ResponseEntity.ok().body("자격증 번호가" + certDeleteReqDto.getCertNumber() + "인 자격증을 삭제했습니다.");
    }

    @PostMapping("/tech")
    public ResponseEntity<String> userProfileTechAdd(@RequestBody ProfileDto.TechRegisterReqDto techReqDto) {
        boolean isPresentTech = profileService.checkTech(techReqDto.getTechName());
        if (isPresentTech) {
            profileService.addUserTech(techReqDto);
            return ResponseEntity.ok().body(techReqDto.getUserNickName() + "님의 프로필에 " + techReqDto.getTechName() + "기술을 등록하였습니다.");
        } else {
                return ResponseEntity.ok().body(techReqDto.getTechName() + "은 등록할 수 없는 기술입니다.");
            }
        }
    @DeleteMapping("/tech")
    public ResponseEntity<String> userProfileTechRemove(@RequestBody ProfileDto.TechDeleteReqDto techDeleteReqDto) {
        profileService.removeUserTech(techDeleteReqDto);
        return ResponseEntity.ok().body(techDeleteReqDto.getUserNickName() + "님의 " + techDeleteReqDto.getTechName() + "기술을 삭제하였습니다.");

    }



}
