package rocket.planet.service.admin;

import static rocket.planet.dto.admin.AdminDeptTeamDto.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.domain.*;
import rocket.planet.dto.admin.AdminDeptTeamDto;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.OrgRepository;
import rocket.planet.repository.jpa.TeamRepository;
import rocket.planet.util.exception.AlreadyExistsTeamException;
import rocket.planet.util.exception.NoSuchDeptException;
import rocket.planet.util.exception.NoSuchTeamException;

@Service
public class AdminTeamService {

	private TeamRepository teamRepository;

	private DeptRepository deptRepository;

	private OrgRepository orgRepository;


	public AdminTeamService(TeamRepository teamRepository, DeptRepository deptRepository, OrgRepository orgRepository) {
		this.teamRepository = teamRepository;
		this.deptRepository = deptRepository;
		this.orgRepository = orgRepository;
	}

	@Transactional
	public AdminResDto addTeam(AdminTeamAddReqDto dto) throws Exception {

		final Department department = Optional.ofNullable(deptRepository.findByDeptName(dto.getDeptName()))
				.orElseThrow(NoSuchDeptException::new);
		Optional.ofNullable(teamRepository.findByTeamName(dto.getTeamName()))
				.ifPresentOrElse(team -> {
					throw new AlreadyExistsTeamException();
				}, () -> {
					Team saveTeam = teamRepository.save(Team.defaultTeam(dto, department.getDeptType(), department));
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

	@Transactional
	public AdminResDto modifyTeamActive(AdminDeptTeamDto.UpdateTeamActiveReqDto activeReqDtoTeam) {
		Team team = teamRepository.findByTeamName(activeReqDtoTeam.getTeamName());
		team.updateTeamInactive();
		Team noTeam = teamRepository.findByTeamName("팀없음");

		List<Org> inactiveOrgs = orgRepository.findByTeam_TeamInactive(true).orElse(Collections.emptyList());
		for (Org org : inactiveOrgs) {
			org.hasNoTeam(noTeam);
		}
		return AdminResDto.builder()
				.message(activeReqDtoTeam.getTeamName() + " 팀은 비활성화되었으며 해당 팀에 속하는 유저들의 '팀없음'으로 소속이 변경되었습니다.")
				.build();

	}
}
