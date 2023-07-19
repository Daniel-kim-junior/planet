package rocket.planet.service.admin;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static rocket.planet.dto.admin.AdminDeptTeamDto.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import rocket.planet.domain.Department;
import rocket.planet.domain.Team;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.TeamRepository;
import rocket.planet.util.exception.AlreadyExistsTeamException;
import rocket.planet.util.exception.NoSuchDeptException;
import rocket.planet.util.exception.NoSuchTeamException;

@SpringBootTest
class AdminTeamServiceTest {

	@InjectMocks
	private AdminTeamService adminTeamService;

	@Mock
	private DeptRepository deptRepository;

	@Mock
	private TeamRepository teamRepository;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		final Department department = Department.builder().deptName("부서 있음").build();

		when(deptRepository.findByDeptName("부서 있음")).thenReturn(department);

		when(deptRepository.findByDeptName("dktechin")).thenReturn(null);

		when(teamRepository.findByTeamName("dk")).thenReturn(Team.builder().build());

		when(teamRepository.findByTeamName("dking")).thenReturn(null);

	}

	@Test
	void 팀_생성_테스트() throws Exception {

		// dto가 null일 경우
		assertThrows(Exception.class, () -> adminTeamService.addTeam(null));

		// 팀의 부서가 존재하지 않을 경우
		assertThrows(NoSuchDeptException.class,
			() -> adminTeamService.addTeam(
				AdminTeamAddReqDto.builder()
					.deptName("dktechin")
					.teamName("dk")
					.build()));

		// 팀의 부서가 존재하나 팀이 이미 존재하는 경우
		assertThrows(AlreadyExistsTeamException.class, () -> {
			adminTeamService.addTeam(
				AdminTeamAddReqDto.builder()
					.deptName("부서 있음")
					.teamName("dk")
					.build());
		});

		// 팀의 부서가 존재하고 팀이 존재하지 않는 경우 (등록 완료)
		final AdminResDto dto = adminTeamService.addTeam(
			AdminTeamAddReqDto.builder()
				.deptName("부서 있음")
				.teamName("dking")
				.build());

		assertThat(dto.getMessage()).isEqualTo("팀이 추가되었습니다");
	}

	@Test
	void 팀_수정_테스트() {
		// dto가 null일 경우
		assertThrows(Exception.class, () -> adminTeamService.modifyTeam(null));

		// 팀이 존재하지 않을 경우
		assertThrows(NoSuchTeamException.class, () -> adminTeamService.modifyTeam(
			AdminTeamModReqDto.builder()
				.targetName("dking")
				.build()));

		// 팀이 존재하나 변경할 이름이 이미 존재하는 경우
		assertThrows(AlreadyExistsTeamException.class, () -> adminTeamService.modifyTeam(
			AdminTeamModReqDto.builder()
				.targetName("dk")
				.changeName("dk")
				.build()));
	}

}
