package rocket.planet.service.admin;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rocket.planet.dto.AdminTempDto.AdminReqDto;
import rocket.planet.dto.AdminTempDto.AdminResDto;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.TeamRepository;
import rocket.planet.util.exception.TeamAlreadyExistsException;

@Service
@RequiredArgsConstructor
public class AdminTeamService {

	private final TeamRepository teamRepository;

	private final DeptRepository deptRepository;

	public AdminResDto removeTeam(AdminReqDto dto) throws Exception {

		teamRepository.deleteByTeamName(dto.getName());

		return AdminResDto.builder().message("팀이 삭제되었습니다").build();
	}

	public AdminResDto modifyTeam(AdminReqDto dto) throws Exception {

		return null;
	}

	public AdminResDto addTeam(AdminReqDto dto) throws Exception {

		Optional.of(teamRepository.findByTeamName(dto.getName()))
			.ifPresentOrElse(team -> {
				throw new TeamAlreadyExistsException();
			}, () -> {

			});

		return null;
	}
}
