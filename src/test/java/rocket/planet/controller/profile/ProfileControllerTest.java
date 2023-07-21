package rocket.planet.controller.profile;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;

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
class ProfileControllerTest {

	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private ProfileService profileService;

	@DisplayName("유저 프로필 수정")
	@Test
	@Transactional
	void 유저_프로필_수정_테스트() {
		Profile crew = profileRepository.findByUserNickName("crew").get();
		ProfileDto.ProfileUpDateResDto profileUpdate = ProfileDto.ProfileUpDateResDto.builder()
			.userNickName(crew.getUserNickName())
			.userName("크루킴")
			.profileStartDate(LocalDate.of(2022, 03, 22))
			.profileCareer(3)
			.profileBirthDt(LocalDate.of(1999, 8, 23))
			.build();
		 profileService.modifyProfile(profileUpdate, crew.getUserNickName());
		assertThat(profileRepository.findByUserNickName("crew"));
	}

	@Test
	@Transactional
	void 유저_프로필_공개범위_수정_테스트() {
		Profile crew = profileRepository.findByUserNickName("crew").get();
		ProfileDto.ProfileDisplayUpDateResDto displayUpdate = ProfileDto.ProfileDisplayUpDateResDto.builder()
			.userNickName(crew.getUserNickName())
			.profileDisplay(true)
			.build();
		 profileService.modifyProfileDisplay(displayUpdate, crew.getUserNickName());
		assertThat(profileRepository.findByUserNickName("crew"));
	}

	@Test
	@Transactional
	void 유저_프로필_휴가_수정_테스트() {
		Profile crew = profileRepository.findByUserNickName("crew").get();
		ProfileDto.AnnualUpDateResDto annualUpdate = ProfileDto.AnnualUpDateResDto.builder()
			.userNickName(crew.getUserNickName())
			.profileAnnualStatus(true)
			.build();
		 profileService.modifyAnnualStatus(annualUpdate,crew.getUserNickName());
		assertThat(profileRepository.findByUserNickName("crew"));
	}




}