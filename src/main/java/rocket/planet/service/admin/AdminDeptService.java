package rocket.planet.service.admin;

import static rocket.planet.dto.admin.AdminDeptTeamDto.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.domain.*;
import rocket.planet.dto.admin.AdminDeptTeamDto;
import rocket.planet.repository.jpa.CompanyRepository;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.OrgRepository;
import rocket.planet.repository.jpa.TeamRepository;
import rocket.planet.util.exception.AlreadyExistsDeptException;
import rocket.planet.util.exception.NoSuchCompanyException;
import rocket.planet.util.exception.NoSuchDeptException;

@Service
public class AdminDeptService {

	private DeptRepository deptRepository;

	private CompanyRepository companyRepository;

	private OrgRepository orgRepository;

	private TeamRepository teamRepository;

	public AdminDeptService(DeptRepository deptRepository, CompanyRepository companyRepository, OrgRepository orgRepository, TeamRepository teamRepository) {
		this.deptRepository = deptRepository;
		this.companyRepository = companyRepository;
		this.orgRepository = orgRepository;
		this.teamRepository = teamRepository;
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
		List<Team> inactiveTeams = teamRepository.findTeamNameByDeptName(dept.getDeptName());
		for (Team team : inactiveTeams) {
			team.updateTeamInactive();
		}
		List<Org> inactiveOrgs = orgRepository.findByDepartment_DeptInactive(true).orElse(Collections.emptyList());
		for (Org org : inactiveOrgs) {
			orgRepository.delete(org);
		}
		return AdminResDto.builder()
				.message(activeReqDtoDept.getDeptName() + " 부문은 비활성화되었으며 [ " + activeReqDtoDept.getDeptName() + " ] 부문에 속한 사원들은 현재 소속된 부문과 팀이 없습니다.")
				.build();
	}
}
