package rocket.planet.controller.profile;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import rocket.planet.dto.profile.ProfileDto;
import rocket.planet.repository.jpa.UserPjtRepository;
import rocket.planet.service.profile.ProfileService;

@SpringBootTest
class UserProjectControllerTest {

	@Autowired
	private ProfileService profileService;
	@Autowired
	private UserPjtRepository userPjtRepository;

	@DisplayName("내부 프로젝트 테스트")
	@Test
	@Rollback(false)
	void 내부프로젝트_상세정보_수정_및_작성_테스트() {
		ProfileDto.insideProjectUpdateReqDto updateInsidePjt = ProfileDto.insideProjectUpdateReqDto.builder()
			.userNickName("plpl")
			.projectName("스마트 시티 TF")
			.projectDesc("아주 상당히 굉장히 엄청나게 스마트한 도시를 만들었어요.")
			.build();
		// profileService.modifyUserInsideProject(updateInsidePjt);
		assertThat(userPjtRepository.findByProject_ProjectNameAndAndProfile_UserNickName("스마트 시티 TF", "plpl"));
	}

}