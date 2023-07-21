package rocket.planet.controller.profile;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.domain.Profile;
import rocket.planet.dto.profile.ProfileDto;
import rocket.planet.repository.jpa.CertRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.service.profile.ProfileService;

@SpringBootTest
class CertificationControllerTest {

	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private ProfileService profileService;
	@Autowired
	private CertRepository certRepository;

	@DisplayName("자격증 생성 테스트")
	@Test
	@Rollback(false)
	void 자격증_생성_테스트() {
		Profile admin = profileRepository.findByUserNickName("admin").get();
		Profile crew = profileRepository.findByUserNickName("crew").get();
		Profile pilot = profileRepository.findByUserNickName("pilot").get();

		ProfileDto.CertRegisterResDto cert1 = ProfileDto.CertRegisterResDto.builder()
			.userNickName(admin.getUserNickName())
			.certName("HSK6급")
			.certDt(LocalDate.of(2022, 11, 6))
			.certAgency("汉办")
			.certExpireDate(LocalDate.of(2024, 11, 7))
			.certType("언어")
			.certNumber("H62120495837496837")
			.build();
		ProfileDto.CertRegisterResDto cert2 = ProfileDto.CertRegisterResDto.builder()
			.userNickName(admin.getUserNickName())
			.certName("OPIC")
			.certDt(LocalDate.of(2022, 7, 6))
			.certAgency("YBM")
			.certExpireDate(LocalDate.of(2024, 7, 8))
			.certType("언어")
			.certNumber("O294850284")
			.build();
		ProfileDto.CertRegisterResDto cert3 = ProfileDto.CertRegisterResDto.builder()
			.userNickName(crew.getUserNickName())
			.certName("sqld")
			.certDt(LocalDate.of(2021, 5, 3))
			.certAgency("한국데이터산업진흥원")
			.certExpireDate(null)
			.certType("IT 자격증")
			.certNumber("2835820")
			.build();
		ProfileDto.CertRegisterResDto cert4 = ProfileDto.CertRegisterResDto.builder()
			.userNickName(pilot.getUserNickName())
			.certName("정보처리기사")
			.certDt(LocalDate.of(2022, 5, 3))
			.certAgency("HRDK 한국산업인력공단")
			.certExpireDate(null)
			.certType("IT 자격증")
			.certNumber("49582038")
			.build();
		ProfileDto.CertRegisterResDto cert5 = ProfileDto.CertRegisterResDto.builder()
			.userNickName(crew.getUserNickName())
			.certName("정보처리기사")
			.certDt(LocalDate.of(2023, 8, 3))
			.certAgency("HRDK 한국산업인력공단")
			.certExpireDate(null)
			.certType("IT 자격증")
			.certNumber("59302910")
			.build();
		// profileService.addCertification(cert1);
		// profileService.addCertification(cert2);
		// profileService.addCertification(cert3);
		// profileService.addCertification(cert4);
		// profileService.addCertification(cert5);

	}

	@Test
	@Transactional
	void 자격증_삭제_테스트() {
		// profileService.removeCertification("5bb8351d-66c3-438c-a23d-5b26855a22c9");
	}

}