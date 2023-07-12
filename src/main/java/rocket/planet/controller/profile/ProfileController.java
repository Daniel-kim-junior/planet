package rocket.planet.controller.profile;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.service.profile.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocket.planet.dto.profile.ProfileDto;

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
    public ResponseEntity<String> outsideProjectRemove(String pjtName) {
        profileService.removeOutsideProject(pjtName);
        return ResponseEntity.ok().body(pjtName + "프로젝트를 삭제하였습니다.");
    }

}
