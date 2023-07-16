package rocket.planet.service.admin;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import rocket.planet.domain.Company;
import rocket.planet.domain.Department;
import rocket.planet.dto.admin.AdminDeptTeamDto;
import rocket.planet.dto.admin.AdminDeptTeamDto.AdminResDto;
import rocket.planet.repository.jpa.CompanyRepository;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.util.exception.AlreadyExistsDeptException;
import rocket.planet.util.exception.NoSuchDeptException;

@SpringBootTest
class AdminDeptServiceTest {

	@InjectMocks
	private AdminDeptService adminDeptService;

	@Mock
	private DeptRepository deptRepository;

	@Mock
	private CompanyRepository companyRepository;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		final Department department = Department.builder().deptName("dktechin").build();

		when(companyRepository.findByCompanyName("dktechin"))
			.thenReturn(Company.builder().companyName("dktechin").build());
		when(deptRepository.findByDeptName("dktechin")).thenReturn(department);
		when(deptRepository.findByDeptName("dktechi")).thenReturn(null);

	}

	@Test
	void 부문_생성_테스트() throws Exception {
		// dto가 null일 경우
		assertThrows(Exception.class, () -> adminDeptService.addDept(null));

		// 부문이 이미 존재할 경우
		assertThrows(AlreadyExistsDeptException.class, () -> adminDeptService.addDept(
			AdminDeptTeamDto.AdminDeptAddReqDto.builder().name("dktechin").deptType("DEVELOPMENT").build()));

		// 겹치는 부문이 없는 경우
		final AdminResDto adminResDto = adminDeptService.addDept(
			AdminDeptTeamDto.AdminDeptAddReqDto.builder().name("dktechi").deptType("DEVELOPMENT").build());

		assertThat(adminResDto.getMessage()).isEqualTo("삽입 완료");
	}

	@Test
	void 부문_수정_테스트() throws Exception {
		// dto가 null일 경우
		assertThrows(Exception.class, () -> adminDeptService.modifyDept(null));

		// 해당 부문이 존재하지 않는 경우
		assertThrows(NoSuchDeptException.class, () -> adminDeptService.modifyDept(
			AdminDeptTeamDto.AdminDeptModReqDto.builder()
				.changeName("dk").targetName("dktech").build()));

		// 부문이 존재하지만 변경할 부문이 이미 존재하는 경우
		assertThrows(AlreadyExistsDeptException.class, () -> adminDeptService.modifyDept(
			AdminDeptTeamDto.AdminDeptModReqDto.builder().targetName("dktechin")
				.changeName("dktechin").build()));

		// 부문이 존재하고 변경할 이름이 유효한 경우
		assertThat(adminDeptService.modifyDept(
			AdminDeptTeamDto.AdminDeptModReqDto.builder().targetName("dktechin")
				.changeName("dktechi").build()).getMessage()).isEqualTo("수정 완료");
	}

	@Test
	void 부문_삭제_테스트() throws Exception {
		// dto가 null일 경우
		assertThrows(Exception.class, () -> adminDeptService.removeDept(null));

		// 해당 부문이 존재하지 않는 경우
		assertThrows(NoSuchDeptException.class, () -> adminDeptService.removeDept(
			AdminDeptTeamDto.AdminDeptTeamDelReqDto.builder().name("dk").build()));

		// 부문이 존재하는 경우 삭제 완료
		assertThat(adminDeptService.removeDept(
			AdminDeptTeamDto.AdminDeptTeamDelReqDto.builder().name("dktechin").build()).getMessage()).isEqualTo(
			"삭제 완료");

	}

}