package rocket.planet.service.stats;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static rocket.planet.service.stats.Stat.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import rocket.planet.domain.Department;
import rocket.planet.domain.Org;
import rocket.planet.domain.OrgType;
import rocket.planet.domain.Profile;
import rocket.planet.domain.ProfileTech;
import rocket.planet.domain.Project;
import rocket.planet.domain.Team;
import rocket.planet.domain.Tech;
import rocket.planet.domain.UserProject;
import rocket.planet.dto.stats.DeptStatsReqDto;
import rocket.planet.dto.stats.EntireStatsReqDto;
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

@SpringBootTest
class StatsServiceTest {
	@InjectMocks
	private StatsService statsService;

	@Mock
	private DeptRepository deptRepository;

	@Mock
	private ProfileRepository profileRepository;

	@Mock
	private PfTechRepository pfTechRepository;

	@Mock
	private UserPjtRepository userPjtRepository;

	@Mock
	private OrgRepository orgRepository;

	@Mock
	private TeamRepository teamRepository;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		/**
		 * 부문 통계 테스트
		 */
		when(deptRepository.findByDeptName("테스트부문")).thenReturn(
			Department.builder().deptType(OrgType.DEVELOPMENT).deptName("테스트부문").build());

		when(deptRepository.findByDeptName("테스트부문비개발")).thenReturn(
			Department.builder().deptType(OrgType.NON_DEVELOPMENT).deptName("테스트부문비개발").build());

		when(teamRepository.findByTeamName("테스트팀")).thenReturn(Team.builder().teamName("테스트팀").build());

		when(pfTechRepository.findTechStatsByProfileDepartment("테스트부문")).thenReturn(
			List.of(ProfileTech.builder().tech(Tech.builder().techName("s").build()).build()));

		when(pfTechRepository.findTechStatsByProfileDepartment("테스트부문비개발")).thenReturn(
			List.of(ProfileTech.builder().tech(Tech.builder().techName("s").build()).build()));

		when(profileRepository.findCareerStatsByDepartment("테스트부문")).thenReturn(
			List.of(Profile.builder().profileCareer(5).build()));

		when(profileRepository.findCareerStatsByDepartment("테스트부문비개발")).thenReturn(
			List.of(Profile.builder().profileCareer(5).build()));

		when(orgRepository.findTeamStatsByDeptName("테스트부문")).thenReturn(
			List.of(mock(Org.class), mock(Org.class), mock(Org.class)));

		when(orgRepository.findTeamStatsByDeptName("테스트부문비개발")).thenReturn(
			List.of(mock(Org.class), mock(Org.class), mock(Org.class)));

		when(userPjtRepository.findPjtPartCountByDepartment("테스트부문")).thenReturn(
			List.of(UserProject.builder().project(Project.builder().projectName("g").build()).build()));

		when(userPjtRepository.findPjtPartCountByDepartment("테스트부문비개발")).thenReturn(
			List.of(UserProject.builder().project(Project.builder().projectName("g").build()).build()));

		// when(userPjtRepository.findProjectStatsByDept("테스트부문")).thenReturn(
		// 	List.of(UserProject.builder().project(Project.builder().projectName("g").build()).build()));
		//
		// when(userPjtRepository.findProjectStatsByDept("테스트부문비개발")).thenReturn(
		// 	List.of(UserProject.builder().project(Project.builder().projectName("g").build()).build()));

		/**
		 * 팀 통계 테스트
		 */
		when(teamRepository.findTeamByDeptName("테스트부문", "테스트팀")).thenReturn(
			Optional.of(Team.builder().teamName("테스트팀").build()));

		when(teamRepository.findTeamByDeptName("테스트부문비개발", "테스트팀비개발")).thenReturn(
			Optional.of(Team.builder().teamName("테스트팀비개발").build()));

		when(pfTechRepository.findTechStatsByProfileTeam("테스트팀"))
			.thenReturn(List.of(ProfileTech.builder().tech(Tech.builder().techName("s").build()).build()));
		when(pfTechRepository.findTechStatsByProfileTeam("테스트팀비개발"))
			.thenReturn(List.of(ProfileTech.builder().tech(Tech.builder().techName("s").build()).build()));
		when(profileRepository.findCareerStatsByTeam("테스트팀"))
			.thenReturn(List.of(Profile.builder().profileCareer(5).build()));
		when(profileRepository.findCareerStatsByTeam("테스트팀비개발"))
			.thenReturn(List.of(Profile.builder().profileCareer(5).build()));
		// when(userPjtRepository.findProjectStatsByTeam("테스트팀"))
		// 	.thenReturn(List.of(UserProject.builder().project(Project.builder().projectName("g").build()).build()));
		// when(userPjtRepository.findProjectStatsByTeam("테스트팀비개발"))
		// 	.thenReturn(List.of(UserProject.builder().project(Project.builder().projectName("g").build()).build()));
		when(userPjtRepository.findPjtPartCountByTeam("테스트팀"))
			.thenReturn(List.of(UserProject.builder().project(Project.builder().projectName("g").build()).build()));
		when(userPjtRepository.findPjtPartCountByTeam("테스트팀비개발"))
			.thenReturn(List.of(UserProject.builder().project(Project.builder().projectName("g").build()).build()));

	}

	@Test
	void 디테일_통계_개발_부문_생성_요청() {

		assertThrows(NoSuchDeptException.class, () -> {
			statsService.getDeptStats(DeptStatsReqDto.builder().deptName("테스트부").unit(3).build());
		});

		assertTrue(
			statsService.isDevelop(Department.builder().deptType(OrgType.DEVELOPMENT).deptName("테스트부문").build()));

		assertFalse(
			statsService.isDevelop(Department.builder().deptType(OrgType.NON_DEVELOPMENT).deptName("테스트부문").build()));

		List<ResponseStatDto> rst = statsService.getDeptStats(
			DeptStatsReqDto.builder().deptName("테스트부문").unit(3).build());
		assertThat(rst.size()).isEqualTo(5);

		String[] labelArray = {"기술별", "경력별", "팀별", "프로젝트별", "프로젝트 참여도"};
		String[] typeArray = {"bar", "bar", "bar", "bar", "circle"};

		for (int i = 0; i < rst.size(); i++) {
			assertThat(rst.get(i).getName()).isEqualTo(labelArray[i]);
			assertThat(rst.get(i).getType()).isEqualTo(typeArray[i]);
		}
	}

	@Test
	void 디테일_통계_비개발_부문_생성_요청() {

		assertThrows(NoSuchDeptException.class, () -> {
			statsService.getDeptStats(DeptStatsReqDto.builder().deptName("테스트부").unit(3).build());
		});

		List<ResponseStatDto> rst = statsService.getDeptStats(
			DeptStatsReqDto.builder().deptName("테스트부문비개발").unit(3).build());
		assertThat(rst.size()).isEqualTo(4);

		String[] labelArray = {"경력별", "팀별", "프로젝트별", "프로젝트 참여도"};
		String[] typeArray = {"bar", "bar", "bar", "circle"};
		for (int i = 0; i < rst.size(); i++) {
			assertThat(rst.get(i).getName()).isEqualTo(labelArray[i]);
			assertThat(rst.get(i).getType()).isEqualTo(typeArray[i]);
		}

	}

	@Test
	void 디테일_통계_개발_팀_생성_요청() throws Exception {

		assertThrows(NoSuchDeptException.class, () -> {
			statsService.getTeamStats(TeamStatsReqDto.builder().deptName("테스트부").teamName("테스트팀").unit(3).build());
		});

		assertThrows(NoSuchTeamException.class, () -> {
			statsService.getTeamStats(TeamStatsReqDto.builder().deptName("테스트부문").teamName("테스").unit(3).build());
		});

		List<ResponseStatDto> rst = statsService.getTeamStats(
			TeamStatsReqDto.builder().deptName("테스트부문").teamName("테스트팀").unit(3).build());
		assertThat(rst.size()).isEqualTo(4);

		String[] labelArray = {"기술별", "경력별", "프로젝트별", "프로젝트 참여도"};
		String[] typeArray = {"bar", "bar", "bar", "circle"};
		for (int i = 0; i < rst.size(); i++) {
			assertThat(rst.get(i).getName()).isEqualTo(labelArray[i]);
			assertThat(rst.get(i).getType()).isEqualTo(typeArray[i]);
		}

	}

	@Test
	void 디테일_통계_비개발_팀_생성_요청() throws Exception {

		assertThrows(NoSuchDeptException.class, () -> {
			statsService.getTeamStats(TeamStatsReqDto.builder().deptName("테스트부").teamName("테스트팀비개발").unit(3).build());
		});

		assertThrows(NoSuchTeamException.class, () -> {
			statsService.getTeamStats(TeamStatsReqDto.builder().deptName("테스트부문비개발").teamName("테스").unit(3).build());
		});

		List<ResponseStatDto> rst = statsService.getTeamStats(
			TeamStatsReqDto.builder().deptName("테스트부문비개발").teamName("테스트팀비개발").unit(3).build());
		assertThat(rst.size()).isEqualTo(3);

		String[] labelArray = {"경력별", "프로젝트별", "프로젝트 참여도"};
		String[] typeArray = {"bar", "bar", "circle"};
		for (int i = 0; i < rst.size(); i++) {
			assertThat(rst.get(i).getName()).isEqualTo(labelArray[i]);
			assertThat(rst.get(i).getType()).isEqualTo(typeArray[i]);
		}

	}

	@Test
	void 전체_통계_테스트() {

		List<ResponseStatDto> rst = statsService.getEntireStats(
			EntireStatsReqDto.builder().companyName("").unit(5).build());

		assertThat(rst.size()).isEqualTo(6);

		String[] labelArray = {"부문별", "기술별", "경력별", "팀별", "프로젝트별", "프로젝트 참여도"};
		String[] typeArray = {"bar", "bar", "bar", "bar", "bar", "circle"};
		for (int i = 0; i < rst.size(); i++) {
			assertThat(rst.get(i).getName()).isEqualTo(labelArray[i]);
			assertThat(rst.get(i).getType()).isEqualTo(typeArray[i]);
		}

	}

	@Test
	void 이분_탐색_테스트() {

		int higherBound = getHigherBound(new ArrayList<>(makeCareerMap().keySet()), 10);
		assertThat(higherBound).isEqualTo(12);

		higherBound = getHigherBound(new ArrayList<>(makeCareerMap().keySet()), 13);
		assertThat(higherBound).isEqualTo(15);

		higherBound = getHigherBound(new ArrayList<>(makeCareerMap().keySet()), 3);
		assertThat(higherBound).isEqualTo(3);

		higherBound = getHigherBound(new ArrayList<>(makeCareerMap().keySet()), -1);
		assertThat(higherBound).isEqualTo(3);
	}

	private Map<String, Integer> makeCareerMap() {

		Map<String, Integer> map = new LinkedHashMap<>();

		for (int i = 0; i < 30; i += 3) {
			if (i == 0) {
				map.put("경력 없음", 0);
			} else {
				map.put(i + "년 이하", 0);
			}
		}

		return map;
	}

}