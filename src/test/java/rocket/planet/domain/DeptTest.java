package rocket.planet.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import rocket.planet.repository.jpa.CompanyRepository;

import rocket.planet.repository.jpa.DeptRepository;

import java.util.UUID;

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

        UUID companyId = company.getId();

        Department smartSolutionDept = Department.builder()
                .company(Company.builder().id(companyId).build())
                .deptName("스마트솔루션")
                .deptType(OrgType.DEVELOPMENT)
                .build();
        deptRepository.saveAndFlush(smartSolutionDept);

        Department smartFactoryDept = Department.builder()
                .company(Company.builder().id(companyId).build())
                .deptName("IT아웃소싱")
                .deptType(OrgType.DEVELOPMENT)
                .build();
        deptRepository.saveAndFlush(smartFactoryDept);

        Department hrDept = Department.builder()
                .company(Company.builder().id(companyId).build())
                .deptName("경영지원")
                .deptType(OrgType.NON_DEVELOPMENT)
                .build();
        deptRepository.saveAndFlush(hrDept);

        Department aiChatbotDept = Department.builder()
                .company(Company.builder().id(companyId).build())
                .deptName("AI챗봇")
                .deptType(OrgType.DEVELOPMENT)
                .build();
        deptRepository.saveAndFlush(aiChatbotDept);

        Department ftDept = Department.builder()
                .company(Company.builder().id(companyId).build())
                .deptName("기업솔루션")
                .deptType(OrgType.DEVELOPMENT)
                .build();
        deptRepository.saveAndFlush(ftDept);



    }
}
