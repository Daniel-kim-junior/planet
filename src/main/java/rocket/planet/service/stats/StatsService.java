package rocket.planet.service.stats;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rocket.planet.domain.Department;
import rocket.planet.domain.Team;
import rocket.planet.dto.stats.DeptStatsReqDto;
import rocket.planet.dto.stats.LabelAndStatDto;
import rocket.planet.dto.stats.ResponseStatDto;
import rocket.planet.dto.stats.TeamStatsReqDto;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.PfTechRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.TeamRepository;
import rocket.planet.repository.jpa.UserPjtRepository;
import rocket.planet.util.exception.NoSuchDeptException;
import rocket.planet.util.exception.NoSuchTeamException;

@Service
@RequiredArgsConstructor
public class StatsService {
	private final DeptRepository deptRepository;
	private final ProfileRepository profileRepository;

	private final PfTechRepository pfTechRepository;

	private final UserPjtRepository userPjtRepository;

	private final TeamRepository teamRepository;

	public List<ResponseStatDto> getDeptStats(DeptStatsReqDto dto) {

		Department department = Optional.ofNullable(deptRepository.findByDeptName(dto.getDeptName()))
			.orElseThrow(NoSuchDeptException::new);

		List<ResponseStatDto> res = new ArrayList<>();

		if (isDevelop(department)) {
			// 개발 부문인 경우
			List<LabelAndStatDto> statList = getDetailStats(department, pfTechRepository,
				TechStats.builder().name("기술별").build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("기술별").labelAndStats(statList).build());

			statList = getDetailStats(department, profileRepository,
				CareerStats.builder().name("경력별").build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("경력별").labelAndStats(statList).build());

			statList = getDetailStats(department, teamRepository,
				TeamStats.builder().name("팀별").build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("팀별").labelAndStats(statList).build());

			statList = getDetailStats(department, userPjtRepository, PjtPartRateStats.builder().name("프로젝트 참여도")
				.build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("프로젝트 참여도").labelAndStats(statList).build());
		} else {
			List<LabelAndStatDto> statList = getDetailStats(department, profileRepository,
				CareerStats.builder().name("경력별").build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("경력별").labelAndStats(statList).build());

			statList = getDetailStats(department, teamRepository,
				TeamStats.builder().name("팀별").build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("팀별").labelAndStats(statList).build());

			statList = getDetailStats(department, userPjtRepository, PjtPartRateStats.builder().name("프로젝트 참여도")
				.build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("프로젝트 참여도").labelAndStats(statList).build());
		}

		return res;
	}

	public List<ResponseStatDto> getTeamStats(TeamStatsReqDto dto) {
		Department department = Optional.ofNullable(deptRepository.findByDeptName(dto.getDeptName()))
			.orElseThrow(NoSuchDeptException::new);

		Team team = Optional.ofNullable(teamRepository.findByTeamName(dto.getTeamName()))
			.orElseThrow(NoSuchTeamException::new);

		// TODO: 2021-07-22 팀별 통계
		if (isDevelop(department)) {

		} else {

		}

		return null;
	}

	private <T extends JpaRepository, E> List<LabelAndStatDto> getDetailStats(E entity, T repository,
		StatCategory category,
		int unit) {
		return Stat.builder()
			.category(category)
			.repository(repository)
			.entity(entity)
			.unit(unit)
			.build()
			.getStats();
	}

	private boolean isDevelop(Department department) {
		return department.getDeptType().name().equals("DEVELOPMENT");
	}

}
