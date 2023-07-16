package rocket.planet.service.admin;

import static rocket.planet.dto.admin.AdminDeptTeamDto.*;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.domain.Company;
import rocket.planet.domain.Department;
import rocket.planet.repository.jpa.CompanyRepository;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.util.exception.AlreadyExistsDeptException;
import rocket.planet.util.exception.NoSuchCompanyException;
import rocket.planet.util.exception.NoSuchDeptException;

@Service
public class AdminDeptService {

	private DeptRepository deptRepository;

	private CompanyRepository companyRepository;

	public AdminDeptService(DeptRepository deptRepository, CompanyRepository companyRepository) {
		this.deptRepository = deptRepository;
		this.companyRepository = companyRepository;
	}

	@Transactional
	public AdminResDto addDept(AdminDeptAddReqDto dto) throws Exception {

		final Company dkTechIn = Optional.ofNullable(companyRepository.findByCompanyName("dktechin"))
			.orElseThrow(NoSuchCompanyException::new);

		Optional.ofNullable(deptRepository.findByDeptName(dto.getName()))
			.ifPresent(e -> {
				throw new AlreadyExistsDeptException();
			});

		deptRepository.save(Department.defaultDept(dto, dkTechIn));

		return AdminResDto.builder().message("삽입 완료").build();
	}

	public AdminResDto removeDept(AdminDeptTeamDelReqDto dto) throws Exception {

		final Department department = Optional.ofNullable(deptRepository.findByDeptName(dto.getName()))
			.orElseThrow(NoSuchDeptException::new);

		deptRepository.delete(department);

		return AdminResDto.builder().message("삭제 완료").build();
	}

	@Transactional
	public AdminResDto modifyDept(AdminDeptModReqDto dto) throws Exception {

		final Department department = Optional.ofNullable(deptRepository.findByDeptName(dto.getTargetName()))
			.orElseThrow(NoSuchDeptException::new);

		Optional.ofNullable(deptRepository.findByDeptName(dto.getChangeName()))
			.ifPresent(e -> {
				throw new AlreadyExistsDeptException();
			});

		department.update(dto.getChangeName());

		return AdminResDto.builder().message("수정 완료").build();
	}
}
