package rocket.planet.service.admin;

import static rocket.planet.dto.admin.AdminDeptTeamDto.*;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.domain.Department;
import rocket.planet.domain.Team;
import rocket.planet.dto.admin.AdminDeptTeamDto.*;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.TeamRepository;
import rocket.planet.util.exception.AlreadyExistsTeamException;
import rocket.planet.util.exception.NoSuchDeptException;
import rocket.planet.util.exception.NoSuchTeamException;

@Service
public class AdminTeamService {

	private TeamRepository teamRepository;

	private DeptRepository deptRepository;

	public AdminTeamService(TeamRepository teamRepository, DeptRepository deptRepository) {
		this.teamRepository = teamRepository;
		this.deptRepository = deptRepository;
	}

	@Transactional
	public AdminResDto addTeam(AdminTeamAddReqDto dto) throws Exception {

		final Department department = deptRepository.findByDeptName(dto.getDeptName());

		if (department == null) {
			throw new NoSuchDeptException();
		}

		Optional.ofNullable(teamRepository.findByTeamName(dto.getTeamName()))
			.ifPresentOrElse(team -> {
				throw new AlreadyExistsTeamException();
			}, () -> {
				Team saveTeam = teamRepository.save(Team.defaultTeam(dto, department.getDeptType()));
				department.addTeam(saveTeam);
			});

		return AdminResDto.builder().message("팀이 추가되었습니다").build();
	}

	@Transactional
	public AdminResDto modifyTeam(AdminTeamModReqDto dto) throws Exception {
		final Team team = Optional.ofNullable(teamRepository.findByTeamName(dto.getTargetName()))
			.orElseThrow(NoSuchTeamException::new);

		Optional.ofNullable(teamRepository.findByTeamName(dto.getChangeName()))
			.ifPresent(e -> {
				throw new AlreadyExistsTeamException();
			});

		team.modifyTeam(dto.getChangeName(), dto.getChangeDesc());

		return AdminResDto.builder().message("팀이 수정되었습니다").build();
	}

	public AdminResDto removeTeam(AdminDeptTeamDelReqDto dto) throws Exception {

		final Team team = Optional.ofNullable(teamRepository.findByTeamName(dto.getName()))
			.orElseThrow(NoSuchTeamException::new);

		teamRepository.delete(team);

		return AdminResDto.builder().message("팀이 삭제되었습니다").build();
	}
}
