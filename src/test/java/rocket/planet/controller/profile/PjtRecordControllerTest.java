package rocket.planet.controller.profile;


import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.domain.Profile;
import rocket.planet.dto.common.CommonResDto;
import rocket.planet.dto.profile.ProfileDto;
import rocket.planet.repository.jpa.PjtRecordRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.service.profile.ProfileService;
import rocket.planet.util.exception.ReqNotFoundException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class PjtRecordControllerTest {

	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private ProfileService profileService;
	@Autowired
	private PjtRecordRepository pjtRecordRepository;

	@DisplayName("외부 프로젝트 생성 테스트")
	@Test
	@Rollback(false)
	void 외부_프로젝트_생성_테스트() {
		Profile admin = profileRepository.findByUserNickName("admin").get();
		Profile crew = profileRepository.findByUserNickName("crew").get();
		Profile pilot = profileRepository.findByUserNickName("pilot").get();
		Profile captain = profileRepository.findByUserNickName("captain").get();
		Profile radar = profileRepository.findByUserNickName("radar").get();
		Profile pl = profileRepository.findByUserNickName("plpl").get();

		ProfileDto.OutsideProjectRegisterReqDto outProject1 = ProfileDto.OutsideProjectRegisterReqDto.builder()
				.userNickName(admin.getUserNickName())
				.pjtName("2022년 상반기 신입사원 공개채용")
				.pjtStartDt(LocalDate.of(2021, 1, 3))
				.pjtEndDt(LocalDate.of(2022, 5, 25))
				.pjtTech("HRM")
				.pjtUserTech("인사팀")
				.pjtDesc("새롭게 바뀐 채용프로세스를 도입한 공개채용 프로젝트").build();
		ProfileDto.OutsideProjectRegisterReqDto outProject2 = ProfileDto.OutsideProjectRegisterReqDto.builder()
				.userNickName(pilot.getUserNickName())
				.pjtName("crewz")
				.pjtStartDt(LocalDate.of(2023, 3, 3))
				.pjtEndDt(LocalDate.of(2023, 3, 25))
				.pjtTech("Spring, Spring Boot, Java")
				.pjtUserTech("Back-End 개발자")
				.pjtDesc("사내 동아리 운영 시스템").build();
		ProfileDto.OutsideProjectRegisterReqDto outProject3 = ProfileDto.OutsideProjectRegisterReqDto.builder()
				.userNickName(captain.getUserNickName())
				.pjtName("crewz-admin")
				.pjtStartDt(LocalDate.of(2023, 5, 3))
				.pjtEndDt(LocalDate.of(2023, 5, 25))
				.pjtTech("Vue, JavaScript, Tailwind")
				.pjtUserTech("Front-End 개발자")
				.pjtDesc("사내 동아리 관리 시스템").build();
		ProfileDto.OutsideProjectRegisterReqDto outProject4 = ProfileDto.OutsideProjectRegisterReqDto.builder()
				.userNickName(radar.getUserNickName())
				.pjtName("REfresh")
				.pjtStartDt(LocalDate.of(2023, 3, 3))
				.pjtEndDt(LocalDate.of(2023, 3, 25))
				.pjtTech("Spring, Spring Boot, Java")
				.pjtUserTech("Back-End 개발자")
				.pjtDesc("연차 관리 시스템").build();
		ProfileDto.OutsideProjectRegisterReqDto outProject5 = ProfileDto.OutsideProjectRegisterReqDto.builder()
				.userNickName(pl.getUserNickName())
				.pjtName("Synergy")
				.pjtStartDt(LocalDate.of(2023, 3, 3))
				.pjtEndDt(LocalDate.of(2023, 3, 25))
				.pjtTech("Spring, Spring Boot, Java, Vue, JavaScript")
				.pjtUserTech("Full-Stack 개발자")
				.pjtDesc("교육 관리 시스템").build();
		ProfileDto.OutsideProjectRegisterReqDto outProject6 = ProfileDto.OutsideProjectRegisterReqDto.builder()
			.userNickName(crew.getUserNickName())
			.pjtName("dk.log")
			.pjtStartDt(LocalDate.of(2023, 3, 3))
			.pjtEndDt(LocalDate.of(2023, 3, 15))
			.pjtTech("Spring, Spring Boot, Java")
			.pjtUserTech("Back-End 개발자")
			.pjtDesc("dktechin 교육생들을 위한 교육 블로그").build();
		profileService.addOusideProject(outProject1, admin.getUserNickName());
		profileService.addOusideProject(outProject2, pilot.getUserNickName());
		profileService.addOusideProject(outProject3, captain.getUserNickName());
		profileService.addOusideProject(outProject4, radar.getUserNickName());
		profileService.addOusideProject(outProject5, pl.getUserNickName());
		profileService.addOusideProject(outProject6, crew.getUserNickName());

	}

	@Test
	@Transactional
	void 외부프로젝트_수정_테스트() {
		Profile pilot = profileRepository.findByUserNickName("pilot").get();
		ProfileDto.OutsideProjectUpdateReqDto updateProject = ProfileDto.OutsideProjectUpdateReqDto.builder()
				.pjtName("crewz")
				.pjtStartDt(LocalDate.of(2024, 5, 6))
				.pjtEndDt(LocalDate.of(2024, 7, 8))
				.pjtDesc("수정된 외부프로젝트")
				.pjtUserTech("백엔드 개발자")
				.pjtTech("스프링, 자바, 스프링부트")
				.build();
		profileService.modifyOusideProject(updateProject, pilot.getUserNickName());

	}

//	@Test
//	@Transactional
//	void 외부프로젝트_삭제_테스트() {
//		 profileService.removeOutsideProject("56fad2d9-324f-4fdd-a624-5ae3b4440152");
//		String pilotNickname = "pilot";
//		String pjtName = "crewz";
//		Profile pilot = profileRepository.findByUserNickName(pilotNickname).get();
//		UUID pjtUid = pjtRecordRepository.findByPjtName(pjtName).orElseThrow(() -> new ReqNotFoundException("삭제할 외부프로젝트가 존재하지 않습니다.")).getId();
//		String pjtUidString = pjtUid.toString();
//		CommonResDto result = profileService.removeOutsideProject(pjtUidString, pilotNickname);
//		assertEquals(pjtName + "을 삭제했습니다.", result.getMessage());
//
//	}


}