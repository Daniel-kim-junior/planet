package rocket.planet.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import rocket.planet.repository.jpa.CompanyRepository;
import rocket.planet.repository.jpa.DeptRepository;

@SpringBootTest
public class DeptTest {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private DeptRepository deptRepository;

	@Test
	@Rollback(false)
	public void createDepartmentTestData() {

		Company company = Company.builder()
			.companyName("dktechin")
			.build();
		companyRepository.saveAndFlush(company);

		Department smartSolutionDept = Department.builder()
			.company(company)
			.deptName("스마트솔루션")
			.deptType(OrgType.DEVELOPMENT)
			.deptInactive(false)
			.build();
		deptRepository.saveAndFlush(smartSolutionDept);

		Department smartFactoryDept = Department.builder()
			.company(company)
			.deptName("IT아웃소싱")
			.deptInactive(false)
			.deptType(OrgType.DEVELOPMENT)
			.build();
		deptRepository.saveAndFlush(smartFactoryDept);

		Department hrDept = Department.builder()
			.company(company)
			.deptName("경영지원")
			.deptInactive(false)
			.deptType(OrgType.NON_DEVELOPMENT)
			.build();
		deptRepository.saveAndFlush(hrDept);

		Department aiChatbotDept = Department.builder()
			.company(company)
			.deptName("AI챗봇")
			.deptInactive(false)
			.deptType(OrgType.DEVELOPMENT)
			.build();
		deptRepository.saveAndFlush(aiChatbotDept);

		Department ftDept = Department.builder()
			.company(company)
			.deptName("기업솔루션")
			.deptInactive(false)
			.deptType(OrgType.DEVELOPMENT)
			.build();
		deptRepository.saveAndFlush(ftDept);

		Department noneDept = Department.builder()
			.company(company)
			.deptName("부문없음")
			.deptInactive(false)
			.deptType(OrgType.NON_DEVELOPMENT)
			// .deptType(OrgType.NONE)
			.build();
		deptRepository.saveAndFlush(noneDept);

	}
}
