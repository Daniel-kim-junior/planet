package rocket.planet.controller.project;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import rocket.planet.domain.ProjectStatus;
import rocket.planet.dto.project.ProjectRegisterReqDto;
import rocket.planet.dto.project.ProjectRegisterResDto;
import rocket.planet.repository.jpa.ProjectRepository;
import rocket.planet.service.project.ProjectService;

@SpringBootTest
class ProjectControllerTest {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ProjectRepository projectRepository;

	@DisplayName("프로젝트 생성 테스트")
	@Test
	@Rollback(false)
	void 프로젝트_생성_테스트() {
		//
		// private String userName;
		// private String projectName;
		// private String projectDesc;
		// private String projectTech;
		// private LocalDate projectStartDt;
		// private LocalDate projectEndDt;
		// private ProjectStatus projectStatus;
		ProjectRegisterReqDto project = ProjectRegisterReqDto.builder()
			.userName("박피엘")
			.projectName("스마트 시티 TF")
			.projectDesc("스마트 시티에 대한 단기 목표를 달성하기 위해 만들었습니다.")
			.projectTech("Spring, Java, Computer Vision")
			.projectStartDt(LocalDate.of(2023,7,5))
			.projectEndDt(LocalDate.of(2023,8,14))
			.projectStatus(ProjectStatus.WAITING)
			.build();

		ProjectRegisterResDto res = projectService.registerProject(project);
		assertThat(projectRepository.findAll().size()).isEqualTo(1);

	}

	@Test
	void projectUpdate() {
	}
}