package rocket.planet.domain;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.repository.jpa.CertRepository;
import rocket.planet.repository.jpa.ProfileRepository;

@SpringBootTest
@Transactional
public class CertificationTest {
	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private CertRepository certRepository;

	@Test
	void saveCert() {

		Profile admin = profileRepository.findByUserNickName("admin").get();
		Profile crew = profileRepository.findByUserNickName("crew").get();
		Profile pilot = profileRepository.findByUserNickName("pilot").get();

		Certification adminCert1 = Certification.builder()
			.profile(admin)
			.certName("HSK6급")
			.certDt(LocalDate.of(2022, 11, 6))
			.certAgency("汉办")
			.certExpireDate(LocalDate.of(2024, 11, 7))
			.certType("언어")
			.certNumber("H62120495837496837")
			.build();
		certRepository.save(adminCert1);
		Certification adminCert2 = Certification.builder()
			.profile(admin)
			.certName("OPIC")
			.certDt(LocalDate.of(2022, 7, 6))
			.certAgency("YBM")
			.certExpireDate(LocalDate.of(2024, 7, 8))
			.certType("언어")
			.certNumber("O294850284")
			.build();
		certRepository.save(adminCert2);
		Certification crewCert1 = Certification.builder()
			.profile(crew)
			.certName("sqld")
			.certDt(LocalDate.of(2023, 5, 3))
			.certAgency("한국데이터산업진흥원")
			.certExpireDate(null)
			.certType("IT 자격증")
			.certNumber("2835820")
			.build();
		certRepository.save(crewCert1);
		Certification pilotCert1 = Certification.builder()
			.profile(pilot)
			.certName("정보처리기사")
			.certDt(LocalDate.of(2023, 5, 3))
			.certAgency("HRDK 한국산업인력공단")
			.certExpireDate(null)
			.certType("IT 자격증")
			.certNumber("49582038")
			.build();
		certRepository.save(pilotCert1);
	}

}
