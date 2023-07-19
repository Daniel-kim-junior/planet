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
import rocket.planet.dto.stats.EntireStatsReqDto;
import rocket.planet.dto.stats.LabelAndStatDto;
import rocket.planet.dto.stats.ResponseStatDto;
import rocket.planet.dto.stats.TeamStatsReqDto;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.OrgRepository;
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

	private final OrgRepository orgRepository;

	private final TeamRepository teamRepository;

	public List<ResponseStatDto> getDeptStats(DeptStatsReqDto dto) {

		Department department = Optional.ofNullable(deptRepository.findByDeptName(dto.getDeptName()))
			.orElseThrow(NoSuchDeptException::new);

		List<ResponseStatDto> res = new ArrayList<>();

		if (isDevelop(department)) {
			// 개발 부문인 경우
			LabelAndStatDto statList = getDetailStats(department, pfTechRepository,
				TechStats.builder().name("기술별").build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("기술별").labelAndStats(statList).build());

			statList = getDetailStats(department, profileRepository,
				CareerStats.builder().name("경력별").build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("경력별").labelAndStats(statList).build());

			statList = getDetailStats(department, orgRepository,
				TeamStats.builder().name("팀별").build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("팀별").labelAndStats(statList).build());

			statList = getDetailStats(department, userPjtRepository,
				ProjectStats.builder().name("프로젝트별").build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("프로젝트별").labelAndStats(statList).build());

			statList = getDetailStats(department, userPjtRepository, PjtPartRateStats.builder().name("프로젝트 참여도")
				.build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("프로젝트 참여도").labelAndStats(statList).build());
		} else {
			LabelAndStatDto statList = getDetailStats(department, profileRepository,
				CareerStats.builder().name("경력별").build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("경력별").labelAndStats(statList).build());

			statList = getDetailStats(department, orgRepository,
				TeamStats.builder().name("팀별").build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("팀별").labelAndStats(statList).build());

			statList = getDetailStats(department, userPjtRepository,
				ProjectStats.builder().name("프로젝트별").build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("프로젝트별").labelAndStats(statList).build());

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

		List<ResponseStatDto> res = new ArrayList<>();

		// TODO: 2021-07-22 팀별 통계
		if (isDevelop(department)) {
			// 개발 부문인 경우
			LabelAndStatDto statList = getDetailStats(team, pfTechRepository,
				TechStats.builder().name("기술별").build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("기술별").labelAndStats(statList).build());

			statList = getDetailStats(team, profileRepository,
				CareerStats.builder().name("경력별").build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("경력별").labelAndStats(statList).build());

			statList = getDetailStats(team, userPjtRepository,
				ProjectStats.builder().name("프로젝트별").build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("프로젝트별").labelAndStats(statList).build());

			statList = getDetailStats(team, userPjtRepository, PjtPartRateStats.builder().name("프로젝트 참여도")
				.build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("프로젝트 참여도").labelAndStats(statList).build());
		} else {
			LabelAndStatDto statList = getDetailStats(team, profileRepository,
				CareerStats.builder().name("경력별").build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("경력별").labelAndStats(statList).build());

			statList = getDetailStats(team, userPjtRepository,
				ProjectStats.builder().name("프로젝트별").build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("프로젝트별").labelAndStats(statList).build());

			statList = getDetailStats(team, userPjtRepository, PjtPartRateStats.builder().name("프로젝트 참여도")
				.build(), dto.getUnit());
			res.add(ResponseStatDto.builder().name("프로젝트 참여도").labelAndStats(statList).build());
		}

		return res;
	}

	public List<ResponseStatDto> getEntireStats(EntireStatsReqDto dto) {

		List<ResponseStatDto> res = new ArrayList<>();
		EntireStats entireStats = EntireStats.builder().build();

		LabelAndStatDto statList = getDetailStats(entireStats, orgRepository,
			DeptStats.builder().name("부문별").build(), dto.getUnit());
		res.add(ResponseStatDto.builder().name("부문별").labelAndStats(statList).build());

		// 개발 부문인 경우
		statList = getDetailStats(entireStats, pfTechRepository,
			TechStats.builder().name("기술별").build(), dto.getUnit());
		res.add(ResponseStatDto.builder().name("기술별").labelAndStats(statList).build());

		statList = getDetailStats(entireStats, profileRepository,
			CareerStats.builder().name("경력별").build(), dto.getUnit());
		res.add(ResponseStatDto.builder().name("경력별").labelAndStats(statList).build());

		statList = getDetailStats(entireStats, orgRepository,
			TeamStats.builder().name("팀별").build(), dto.getUnit());
		res.add(ResponseStatDto.builder().name("팀별").labelAndStats(statList).build());

		statList = getDetailStats(entireStats, userPjtRepository,
			ProjectStats.builder().name("프로젝트별").build(), dto.getUnit());
		res.add(ResponseStatDto.builder().name("프로젝트별").labelAndStats(statList).build());

		statList = getDetailStats(entireStats, userPjtRepository, PjtPartRateStats.builder().name("프로젝트 참여도")
			.build(), dto.getUnit());
		res.add(ResponseStatDto.builder().name("프로젝트 참여도").labelAndStats(statList).build());

		return res;
	}

	private <T extends JpaRepository, E> LabelAndStatDto getDetailStats(E entity, T repository,
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
