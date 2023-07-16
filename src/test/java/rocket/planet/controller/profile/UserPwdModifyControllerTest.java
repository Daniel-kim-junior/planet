package rocket.planet.controller.profile;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import rocket.planet.domain.Profile;
import rocket.planet.domain.Tech;
import rocket.planet.domain.User;
import rocket.planet.dto.profile.ProfileDto;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.TechRepository;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.service.profile.ProfileService;

import java.util.Optional;

@SpringBootTest
class UserPwdModifyControllerTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileService profileService;




    @DisplayName("비밀번호 변경 테스트")
    @Test
    void 비밀번호_변경_확인_테스트() {
        String userId = "crew@gmail.com";
        String newPwd = "crew222!";
        String newPwdCheck = "crew222!";

        Optional<User> crew = userRepository.findByUserId(userId);
        ProfileDto.UserNewPwdReqDto userNewPwdReqDto = ProfileDto.UserNewPwdReqDto.builder()
                .userId(crew.get().getUserId())
                .userPwd(newPwd)
                .userPwdCheck(newPwdCheck)
                .build();
        profileService.changeUserPwd(userNewPwdReqDto);

    }



}