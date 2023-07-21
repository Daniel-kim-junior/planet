package rocket.planet.controller.profile;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.domain.Profile;
import rocket.planet.domain.Tech;
import rocket.planet.dto.profile.ProfileDto;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.TechRepository;
import rocket.planet.service.profile.ProfileService;

@SpringBootTest
class UserTechControllerTest {

	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private ProfileService profileService;
	@Autowired
	private TechRepository techRepository;

	@DisplayName("기술 테스트")
	@Test
	void 기술_등록_테스트() {
		Profile crew = profileRepository.findByUserNickName("crew").get();
		Tech tech = techRepository.findByTechNameIgnoreCase("kotlin").get();
		ProfileDto.TechRegisterReqDto tech1 = ProfileDto.TechRegisterReqDto.builder()
			.userNickName(crew.getUserNickName())
			.techName(tech.getTechName())
			.build();
		// profileService.addUserTech(tech1);

		ProfileDto.TechRegisterReqDto tech2 = ProfileDto.TechRegisterReqDto.builder()
			.userNickName(crew.getUserNickName())
			.techName("아무기술")
			.build();
		// profileService.addUserTech(tech1);

	}

	@Test
	@Transactional
	void 기술_삭제_테스트() {
		// profileService.removeUserTech("12e0f9df-5ad4-4cbe-b93f-f9c340e37858");
	}

}