package rocket.planet.controller.profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import rocket.planet.dto.common.CommonResponse;
import rocket.planet.dto.profile.ProfileDto;
import rocket.planet.service.profile.ProfileService;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/{userNickName}")
    public CommonResponse<String> profileDetails(@PathVariable String userNickName) {
        profileService.getProfileDetailByUserNickName(userNickName);
        return new CommonResponse<>(true, userNickName + "님의 프로필입니다.", null);
    }




}
