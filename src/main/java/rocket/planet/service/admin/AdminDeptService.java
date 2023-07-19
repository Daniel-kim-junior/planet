package rocket.planet.service.admin;

import static rocket.planet.dto.admin.AdminDeptTeamDto.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.domain.Company;
import rocket.planet.domain.Department;
import rocket.planet.domain.Org;
import rocket.planet.domain.OrgType;
import rocket.planet.dto.admin.AdminDeptTeamDto;
import rocket.planet.repository.jpa.CompanyRepository;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.OrgRepository;
import rocket.planet.util.exception.AlreadyExistsDeptException;
import rocket.planet.util.exception.NoSuchCompanyException;
import rocket.planet.util.exception.NoSuchDeptException;

@Service
public class AdminDeptService {

	private DeptRepository deptRepository;

	private CompanyRepository companyRepository;

	private OrgRepository orgRepository;

	public AdminDeptService(DeptRepository deptRepository, CompanyRepository companyRepository, OrgRepository orgRepository) {
		this.deptRepository = deptRepository;
		this.companyRepository = companyRepository;
		this.orgRepository = orgRepository;
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

	@Transactional
	public AdminResDto modifyDeptActive(AdminDeptTeamDto.UpdateDeptActiveReqDto activeReqDtoDept) {
		Department dept = deptRepository.findByDeptName(activeReqDtoDept.getDeptName());
		dept.updateDeptInactive();
		Department noDept = deptRepository.findByDeptName("부문없음");
		List<Org> inactiveOrgs = orgRepository.findByDepartment_DeptInactive(true).orElse(Collections.emptyList());
		for (Org org : inactiveOrgs) {
			org.hasNoDept(noDept);
		}
		return AdminResDto.builder()
				.message(activeReqDtoDept.getDeptName() + " 부문은 비활성화되었으며 해당 부문에 속하는 유저들의 '부문없음'으로 소속이 변경되었습니다.")
				.build();
	}
}
