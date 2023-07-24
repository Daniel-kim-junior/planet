package rocket.planet.controller.profile;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.domain.Profile;
import rocket.planet.dto.profile.ProfileDto;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.service.profile.ProfileService;

@SpringBootTest
@Transactional
class ProfileLogControllerTest {

	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private ProfileService profileService;

	@DisplayName("로그 테스트")
	@Test
	void 로그_등록_테스트() {
		Profile crew = profileRepository.findByUserNickName("crew").get();
		Profile plpl = profileRepository.findByUserNickName("plpl").get();

		ProfileDto.VisitorReqDto log1 = ProfileDto.VisitorReqDto.builder()
			.ownerNickName(plpl.getUserNickName())
			.visitorNickName(crew.getUserNickName())
			.build();
		profileService.addProfileVisitor(log1);
		ProfileDto.VisitorReqDto log2 = ProfileDto.VisitorReqDto.builder()
			.ownerNickName(crew.getUserNickName())
			.visitorNickName(crew.getUserNickName())
			.build();
		profileService.addProfileVisitor(log2);
		ProfileDto.VisitorReqDto log3 = ProfileDto.VisitorReqDto.builder()
			.ownerNickName(plpl.getUserNickName())
			.visitorNickName(crew.getUserNickName())
			.build();
		profileService.addProfileVisitor(log3);
		ProfileDto.VisitorReqDto log4 = ProfileDto.VisitorReqDto.builder()
			.ownerNickName(crew.getUserNickName())
			.visitorNickName(plpl.getUserNickName())
			.build();
		profileService.addProfileVisitor(log4);

	}

}