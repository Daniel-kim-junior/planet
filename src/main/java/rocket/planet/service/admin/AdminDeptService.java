package rocket.planet.service.admin;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import rocket.planet.domain.Company;
import rocket.planet.domain.Department;
import rocket.planet.dto.AdminTempDto;
import rocket.planet.dto.AdminTempDto.AdminResDto;
import rocket.planet.repository.jpa.CompanyRepository;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.util.exception.NoSuchDeptException;

@Service
@RequiredArgsConstructor
public class AdminDeptService {

	private DeptRepository deptRepository;

	private CompanyRepository companyRepository;

	public AdminResDto addDept(AdminTempDto.AdminReqDto dto) throws Exception {
		Company dkteckin = companyRepository.findByCompanyName("dkteckin");
		deptRepository.save(Department.defaultDept(dto.getName(), dkteckin));

		return AdminResDto.builder().message("삽입 완료").build();
	}

	@Transactional
	public AdminResDto removeDept(AdminTempDto.AdminReqDto dto) throws Exception {

		deptRepository.deleteByDeptName(dto.getName());

		return AdminResDto.builder().message("삭제 완료").build();
	}

	@Transactional
	public AdminResDto modifyDept(AdminTempDto.AdminReqDto dto) {
		Department department = Optional.of(deptRepository.findByDeptName(dto.getName()))
			.orElseThrow(NoSuchDeptException::new);

		department.update(dto.getName());

		return AdminResDto.builder().message("수정 완료").build();
	}
}
