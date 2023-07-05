package rocket.planet.controller.project;

<<<<<<< HEAD
import static org.assertj.core.api.AssertionsForClassTypes.*;
=======
import static org.assertj.core.api.Assertions.*;
>>>>>>> 5ee85dc ([PN-51] feat: 프로젝트 생성관련 테스트코드 구현)
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

<<<<<<< HEAD
=======
import org.assertj.core.api.Assertions;
>>>>>>> 5ee85dc ([PN-51] feat: 프로젝트 생성관련 테스트코드 구현)
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
<<<<<<< HEAD
import org.springframework.test.annotation.Rollback;

import rocket.planet.domain.ProjectStatus;
=======
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.domain.Project;
>>>>>>> 5ee85dc ([PN-51] feat: 프로젝트 생성관련 테스트코드 구현)
import rocket.planet.dto.project.ProjectRegisterReqDto;
import rocket.planet.dto.project.ProjectRegisterResDto;
import rocket.planet.repository.jpa.ProjectRepository;
import rocket.planet.service.project.ProjectService;

<<<<<<< HEAD
=======
@Transactional
>>>>>>> 5ee85dc ([PN-51] feat: 프로젝트 생성관련 테스트코드 구현)
@SpringBootTest
class ProjectControllerTest {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ProjectRepository projectRepository;

	@DisplayName("프로젝트 생성 테스트")
	@Test
<<<<<<< HEAD
	@Rollback(false)
	void 프로젝트_생성_테스트() {
		ProjectRegisterReqDto project1 = ProjectRegisterReqDto.builder()
			.userName("박피엘")
			.projectName("스마트 시티 TF")
			.projectDesc("스마트 시티에 대한 단기 목표를 달성하기 위해 만들었습니다.")
			.projectTech("Spring, Java, Computer Vision")
=======
	void 프로젝트_생성_테스트() {
		ProjectRegisterReqDto project = ProjectRegisterReqDto.builder()
			.projectName("스마트 시티 TF")
			.projectDesc("스마트 시티에 대한 단기 목표를 달성하기 위해 만들었습니다.")
			.projectTech("Spring, Java, Computer Vision")
			.userName("aster.isk")
>>>>>>> 5ee85dc ([PN-51] feat: 프로젝트 생성관련 테스트코드 구현)
			.projectStartDt(LocalDate.of(2023,7,5))
			.projectEndDt(LocalDate.of(2023,8,14))
			.build();

<<<<<<< HEAD
		ProjectRegisterReqDto project2 = ProjectRegisterReqDto.builder()
			.userName("파일럿")
			.projectName("스마트 챗봇을 이용한 의료 시스템")
			.projectDesc("의료 시스템에 스마트 챗봇을 이용하여 효율적으로 진료를 할 수 있게 합니다.")
			.projectTech("C++, Python, NLP")
			.projectStartDt(LocalDate.of(2023,7,6))
			.projectEndDt(LocalDate.of(2023,8,20))
			.build();

		projectService.registerProject(project1);
		projectService.registerProject(project2);

		assertThat(projectRepository.findAll().size()).isEqualTo(2);

	}

	@Test
	void projectUpdate() {
	}
=======
		ProjectRegisterResDto res = projectService.registerProject(project);
		assertThat(projectRepository.findAll().size()).isEqualTo(1);

	}

>>>>>>> 5ee85dc ([PN-51] feat: 프로젝트 생성관련 테스트코드 구현)
}