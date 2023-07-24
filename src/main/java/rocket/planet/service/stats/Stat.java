package rocket.planet.service.stats;

import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import lombok.Builder;
import lombok.NoArgsConstructor;
import rocket.planet.domain.Department;
import rocket.planet.domain.Org;
import rocket.planet.domain.Profile;
import rocket.planet.domain.ProfileTech;
import rocket.planet.domain.Team;
import rocket.planet.domain.UserProject;
import rocket.planet.dto.stats.LabelAndStatDto;
import rocket.planet.repository.jpa.OrgRepository;
import rocket.planet.repository.jpa.PfTechRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.UserPjtRepository;

@NoArgsConstructor(access = PROTECTED)
public class Stat<R extends JpaRepository, T extends StatCategory, E> {

	private R repository;

	private T category;

	private E entity;

	private int unit;

	private Map<String, Integer> map;

	@Builder
	public Stat(R repository, T category, E entity, int unit) {
		this.repository = repository;
		this.category = category;
		this.entity = entity;
		this.unit = unit;
	}

	public LabelAndStatDto getStats() {
		LabelAndStatDto dto = null;
		if (entity instanceof Department) {

			Department department = (Department)entity;

			if (category instanceof TechStats) {

				final List<ProfileTech> profileTechList = ((PfTechRepository)repository).findTechStatsByProfileDepartment(
					(department).getDeptName());
				String techName;
				map = new HashMap<>();
				for (ProfileTech tech : profileTechList) {
					techName = tech.getTech().getTechName();
					if (map.containsKey(techName)) {
						map.put(techName,
							map.get(techName) + 1);
					} else {
						map.put(techName, 1);
					}
				}

				dto = LabelAndStatDto.builder().data(map).build();
			} else if (category instanceof CareerStats) {

				final List<Profile> profileList = ((ProfileRepository)repository).findCareerStatsByDepartment(
					department.getDeptName());
				map = makeCareerMap();

				for (Profile profile : profileList) {
					if (profile.getProfileCareer() <= 0) {
						map.put("경력 없음", map.get("경력 없음") + 1);
					} else {
						int higherBound = getHigherBound(new ArrayList<>(map.keySet()), profile.getProfileCareer());
						map.put(higherBound + "년 이하", map.get(higherBound + "년 이하") + 1);
					}
				}
				dto = LabelAndStatDto.builder().data(map).build();
			} else if (category instanceof TeamStats) {
				map = new HashMap<>();

				final List<Org> orgList = ((OrgRepository)repository).findTeamStatsByDeptName(
					department.getDeptName());

				String teamName;
				Optional<Team> team;
				for (Org org : orgList) {
					team = Optional.ofNullable(org.getTeam());
					if (team.isEmpty()) {
						continue;
					}
					teamName = team.get().getTeamName();
					if (map.containsKey(teamName)) {
						map.put(teamName, map.get(teamName) + 1);
					} else {
						map.put(teamName, 1);
					}
				}

				dto = LabelAndStatDto.builder().data(map).build();

			} else if (category instanceof PjtPartRateStats) {

				map = new HashMap<>();

				final List<UserProject> onWorkingProfiles = ((UserPjtRepository)repository).findPjtPartCountByDepartment(
					department.getDeptName());

				final List<UserProject> noWorkingProfiles = ((UserPjtRepository)repository).findPjtPartCountByDepartmentClosed(
					department.getDeptName());

				map.put("참여 중", onWorkingProfiles.size());
				map.put("미 참여", noWorkingProfiles.size());

				dto = LabelAndStatDto.builder().data(map).build();

			} else if (category instanceof ProjectStats) {

				map = new HashMap<>();

				final List<UserProject> userProjectList = ((UserPjtRepository)repository).findProjectStatsByDept(
					department.getDeptName());

				String projectName;
				for (UserProject userProject : userProjectList) {
					projectName = userProject.getProject().getProjectName();
					if (map.containsKey(projectName)) {
						map.put(projectName, map.get(projectName) + 1);
					} else {
						map.put(projectName, 1);
					}
				}

				dto = LabelAndStatDto.builder().data(map).build();
			}

		} else if (entity instanceof Team) {
			// 팀 통계
			Team team = (Team)entity;
			if (category instanceof TechStats) {

				final List<ProfileTech> profileTechList = ((PfTechRepository)repository).findTechStatsByProfileTeam(
					team.getTeamName());

				String techName;
				map = new HashMap<>();
				for (ProfileTech tech : profileTechList) {
					techName = tech.getTech().getTechName();
					if (map.containsKey(techName)) {
						map.put(techName,
							map.get(techName) + 1);
					} else {
						map.put(techName, 1);
					}
				}
				dto = LabelAndStatDto.builder().data(map).build();

			} else if (category instanceof CareerStats) {

				final List<Profile> profileList = ((ProfileRepository)repository).findCareerStatsByTeam(
					team.getTeamName());
				map = makeCareerMap();
				for (Profile profile : profileList) {
					if (profile.getProfileCareer() <= 0) {
						map.put("경력 없음", map.get("경력 없음") + 1);
					} else {
						int higherBound = getHigherBound(new ArrayList<>(map.keySet()), profile.getProfileCareer());
						map.put(higherBound + "년 이하", map.get(higherBound + "년 이하") + 1);
					}
				}
				dto = LabelAndStatDto.builder().data(map).build();

			} else if (category instanceof ProjectStats) {
				map = new HashMap<>();

				final List<UserProject> userProjectList = ((UserPjtRepository)repository).findProjectStatsByTeam(
					team.getTeamName());

				String projectName;

				for (UserProject userProject : userProjectList) {
					projectName = userProject.getProject().getProjectName();
					if (map.containsKey(projectName)) {
						map.put(projectName, map.get(projectName) + 1);
					} else {
						map.put(projectName, 1);
					}
				}

				dto = LabelAndStatDto.builder().data(map).build();

			} else if (category instanceof PjtPartRateStats) {

				map = new HashMap<>();
				final List<UserProject> onWorkingProfiles = ((UserPjtRepository)repository).findPjtPartCountByTeam(
					team.getTeamName());
				final List<UserProject> noWorkingProfiles = ((UserPjtRepository)repository).findPjtPartCountByTeamClosed(
					team.getTeamName());

				map.put("참여 중", onWorkingProfiles.size());
				map.put("미 참여", noWorkingProfiles.size());
				dto = LabelAndStatDto.builder().data(map).build();
			}

		} else if (entity instanceof EntireStats) {
			if (category instanceof TechStats) {

				final List<ProfileTech> profileTechList = ((PfTechRepository)repository).findTechStatsByProfileEntire();
				String techName;
				map = new HashMap<>();
				for (ProfileTech tech : profileTechList) {
					techName = tech.getTech().getTechName();
					if (map.containsKey(techName)) {
						map.put(techName,
							map.get(techName) + 1);
					} else {
						map.put(techName, 1);
					}
				}
				dto = LabelAndStatDto.builder().data(map).build();

			} else if (category instanceof CareerStats) {

				final List<Integer> profileList = ((ProfileRepository)repository).findCareerStatsByEntire();
				map = makeCareerMap();
				for (Integer career : profileList) {
					if (career <= 0) {
						map.put("경력 없음", map.get("경력 없음") + 1);
					} else {
						int higherBound = getHigherBound(new ArrayList<>(map.keySet()), career);
						map.put(higherBound + "년 이하", map.get(higherBound + "년 이하") + 1);
					}
				}
				dto = LabelAndStatDto.builder().data(map).build();

			} else if (category instanceof TeamStats) {

				map = new HashMap<>();

				final List<Org> orgList = ((OrgRepository)repository).findStatsTeamByEntire();
				String teamName;
				Optional<Team> team;
				for (Org org : orgList) {
					team = Optional.ofNullable(org.getTeam());
					if (team.isEmpty()) {
						continue;
					}
					teamName = team.get().getTeamName();
					if (map.containsKey(teamName)) {
						map.put(teamName, map.get(teamName) + 1);
					} else {
						map.put(teamName, 1);
					}
				}

				dto = LabelAndStatDto.builder().data(map).build();

			} else if (category instanceof PjtPartRateStats) {

				map = new HashMap<>();
				final List<UserProject> onWorkingProfiles = ((UserPjtRepository)repository).findPjtPartCountByEntire();
				final List<UserProject> noWorkingProfiles = ((UserPjtRepository)repository).findPjtPartCountByEntireClosed(
				);

				map.put("참여 중", onWorkingProfiles.size());
				map.put("미 참여", noWorkingProfiles.size());
				dto = LabelAndStatDto.builder().data(map).build();
			} else if (category instanceof DeptStats) {

				map = new HashMap<>();

				final List<Org> orgList = ((OrgRepository)repository).findDeptStatsByEntire();
				String deptName;
				Optional<Department> department;
				for (Org org : orgList) {

					department = Optional.ofNullable(org.getDepartment());

					if (department.isEmpty()) {
						continue;
					}
					deptName = department.get().getDeptName();

					if (map.containsKey(deptName)) {
						map.put(deptName, map.get(deptName) + 1);
					} else {
						map.put(deptName, 1);
					}
				}
				dto = LabelAndStatDto.builder().data(map).build();
			} else if (category instanceof ProjectStats) {

				map = new HashMap<>();

				final List<UserProject> userProjectList = ((UserPjtRepository)repository).findPjtPartCountByEntire();
				String projectName;
				for (UserProject userProject : userProjectList) {
					projectName = userProject.getProject().getProjectName();
					if (map.containsKey(projectName)) {
						map.put(projectName, map.get(projectName) + 1);
					} else {
						map.put(projectName, 1);
					}
				}

				dto = LabelAndStatDto.builder().data(map).build();

			}
		}
		return dto;
	}

	private Map<String, Integer> makeCareerMap() {

		Map<String, Integer> map = new LinkedHashMap<>();

		for (int i = 0; i < 30; i += unit) {
			if (i == 0) {
				map.put("경력 없음", 0);
			} else {
				map.put(i + "년 이하", 0);
			}
		}

		return map;
	}

	public static int getHigherBound(List<String> list, int c) {
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
