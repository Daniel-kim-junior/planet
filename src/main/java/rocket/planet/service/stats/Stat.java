package rocket.planet.service.stats;

import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;

import lombok.Builder;
import lombok.NoArgsConstructor;
import rocket.planet.domain.Department;
import rocket.planet.domain.Profile;
import rocket.planet.domain.ProfileTech;
import rocket.planet.domain.Team;
import rocket.planet.domain.UserProject;
import rocket.planet.dto.stats.LabelAndStatDto;
import rocket.planet.repository.jpa.PfTechRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.TeamRepository;
import rocket.planet.repository.jpa.UserPjtRepository;

@NoArgsConstructor(access = PROTECTED)
public class Stat<R extends JpaRepository, T extends StatCategory, E> {

	private R repository;

	private T category;

	private E entity;

	private int unit;

	private Map<String, Double> map;

	@Builder
	public Stat(R repository, T category, E entity, int unit) {
		this.repository = repository;
		this.category = category;
		this.entity = entity;
		this.unit = unit;
	}

	public List<LabelAndStatDto> getStats() {
		List<LabelAndStatDto> rst = new ArrayList<>();
		if (entity instanceof Department) {
			// 부문 통계
			Department department = (Department)entity;
			if (category instanceof TechStats) {
				// 부문에 해당하는 프로필들의 기술 통계
				final List<ProfileTech> profileTechList = ((PfTechRepository)repository).findTechStatsByProfileDepartment(
					(department).getDeptName());
				double size = profileTechList.size();
				String techName;
				map = new HashMap<>();
				for (ProfileTech tech : profileTechList) {
					techName = tech.getTech().getTechName();
					if (map.containsKey(techName)) {
						map.put(techName,
							map.get(techName) + 1 / size);
					} else {
						map.put(techName, 1 / size);
					}
				}
				rst.add(LabelAndStatDto.builder().data(map).build());

			} else if (category instanceof CareerStats) {
				// 경력별 통계

				final List<Profile> profileList = ((ProfileRepository)repository).findCareerStatsByDepartment(
					(department).getDeptName());
				map = makeCareerMap();

				for (Profile profile : profileList) {
					if (profile.getProfileCareer() <= 0) {
						map.put("경력 없음", map.get("경력 없음") + 1);
					} else {
						int higherBound = getHigherBound(new ArrayList<>(map.keySet()), profile.getProfileCareer());
						map.put(higherBound + "년 이하", map.get(higherBound + "년 이하") + 1);
					}
				}
				rst.add(LabelAndStatDto.builder().data(map).build());
			} else if (category instanceof TeamStats) {
				// 팀별 통계
				map = new HashMap<>();

				final List<Team> teamList = ((TeamRepository)repository).findTeamStatsByDeptName(
					(department).getDeptName());
				double size = teamList.size();
				for (Team team : teamList) {
					if (map.containsKey(team.getTeamName())) {
						map.put(team.getTeamName(), map.get(team.getTeamName()) + 1 / size);
					} else {
						map.put(team.getTeamName(), 1 / size);
					}
				}
				rst.add(LabelAndStatDto.builder().data(map).build());
			} else if (category instanceof PjtPartRateStats) {
				// 프로젝트 참여도 통계
				map = new HashMap<>();
				final List<UserProject> onWorkingProfiles = ((UserPjtRepository)repository).findPjtPartCountByDepartment(
					(department).getDeptName());
				final List<UserProject> noWorkingProfiles = ((UserPjtRepository)repository).findPjtPartCountByDepartment(
					(department).getDeptName());

				double size = onWorkingProfiles.size() + noWorkingProfiles.size();
				size = size == 0 ? 1 : size;
				map.put("참여 중", onWorkingProfiles.size() / size);
				map.put("미 참여", noWorkingProfiles.size() / size);
				rst.add(LabelAndStatDto.builder().data(map).build());
			}

		} else if (entity instanceof Team) {
			// 팀 통계
			Team team = (Team)entity;
			if (category instanceof TechStats) {
				// 부문에 해당하는 프로필들의 기술 통계
				final List<ProfileTech> profileTechList = ((PfTechRepository)repository).findTechStatsByProfileTeam(
					team.getTeamName());
				double size = profileTechList.size();
				String techName;
				map = new HashMap<>();
				for (ProfileTech tech : profileTechList) {
					techName = tech.getTech().getTechName();
					if (map.containsKey(techName)) {
						map.put(techName,
							map.get(techName) + 1 / size);
					} else {
						map.put(techName, 1 / size);
					}
				}
				rst.add(LabelAndStatDto.builder().data(map).build());
			} else if (category instanceof CareerStats) {
				// 경력별 통계

				final List<Profile> profileList = ((ProfileRepository)repository).findCareerStatsByTeam(
					team.getTeamName());

			} else if (category instanceof PjtPartRateStats) {
				// 프로젝트 참여도 통계
				map = new HashMap<>();
				// final List<UserProject> onWorkingProfiles = ((UserPjtRepository)repository).findPjtPartCountByTeam(
				// 	team.getTeamName());
				// final List<UserProject> noWorkingProfiles = ((UserPjtRepository)repository).findPjtPartCountByTeam(
				// 	team.getTeamName());

				// double size = onWorkingProfiles.size() + noWorkingProfiles.size();
				// size = size == 0 ? 1 : size;
				// map.put("참여 중", onWorkingProfiles.size() / size);
				// map.put("미 참여", noWorkingProfiles.size() / size);
				// rst.add(LabelAndStatDto.builder().data(map).build());
			}

		}
		return rst;
	}

	private Map<String, Double> makeCareerMap() {

		Map<String, Double> map = new LinkedHashMap<>();

		for (int i = 0; i < 30; i += unit) {
			if (i == 0) {
				map.put("경력 없음", 0.0);
			} else {
				map.put(i + "년 이하", 0.0);
			}
		}

		return map;
	}

	private int getHigherBound(List<String> list, int c) {
		int lo = 1;
		int hi = list.size() - 1;
		while (lo <= hi) {
			int mid = (hi + lo) / 2;
			if (c < Integer.parseInt(list.get(mid).split("년")[0])) {
				hi = mid - 1;
			} else if (c > Integer.parseInt(list.get(mid).split("년")[0])) {
				lo = mid + 1;
			} else {
				return Integer.parseInt(list.get(mid).split("년")[0]);
			}
		}
		return Integer.parseInt(list.get(lo).split("년")[0]);
	}

}
