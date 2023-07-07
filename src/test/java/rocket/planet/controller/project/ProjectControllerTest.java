package rocket.planet.controller.project;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static rocket.planet.dto.project.ProjectUpdateDto.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.dto.project.ProjectRegisterReqDto;
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
		ProjectRegisterReqDto project1 = ProjectRegisterReqDto.builder()
			.userName("강캡틴")
			.projectName("스마트 시티 TF")
			.projectDesc("스마트 시티에 대한 단기 목표를 달성하기 위해 만들었습니다.")
			.projectTech("Spring, Java, Computer Vision")
			.projectStartDt(LocalDate.of(2023, 7, 5))
			.projectEndDt(LocalDate.of(2023, 8, 14))
			.build();

		ProjectRegisterReqDto project2 = ProjectRegisterReqDto.builder()
			.userName("파일럿")
			.projectName("스마트 챗봇을 이용한 의료 시스템")
			.projectDesc("의료 시스템에 스마트 챗봇을 이용하여 효율적으로 진료를 할 수 있게 합니다.")
			.projectTech("C++, Python, NLP")
			.projectStartDt(LocalDate.of(2023, 7, 7))
			.projectEndDt(LocalDate.of(2023, 8, 20))
			.build();

		ProjectRegisterReqDto project3 = ProjectRegisterReqDto.builder()
			.userName("레이더")
			.projectName("스마트 솔루션 TF")
			.projectDesc("스마트 솔루션의 전반적인 목표를 달성하기 위한 프로젝트입니다.")
			.projectTech("Python, PyTorch, C++, TensorFlow, MySQL")
			.projectStartDt(LocalDate.of(2023, 7, 15))
			.projectEndDt(LocalDate.of(2023, 12, 2))
			.build();

		ProjectRegisterReqDto project4 = ProjectRegisterReqDto.builder()
			.userName("강캡틴")
			.projectName("스마트 건설 TF")
			.projectDesc("건설 현장에서 카카오톡을 이용하여 관리를 용이하게 합니다.")
			.projectTech("iOS, Android, Kotlin, MySQL, MongoDB")
			.projectStartDt(LocalDate.of(2023, 7, 8))
			.projectEndDt(LocalDate.of(2023, 9, 12))
			.build();

		ProjectRegisterReqDto project5 = ProjectRegisterReqDto.builder()
			.userName("파일럿")
			.projectName("카카오톡 IT 솔루션")
			.projectDesc("카카오톡의 내부 기능을 추가하기 위한 프로젝트입니다.")
			.projectTech("iOS, Android, Kotlin, Swift, React Native")
			.projectStartDt(LocalDate.of(2023, 6, 8))
			.projectEndDt(LocalDate.of(2023, 8, 7))
			.build();

		projectService.registerProject(project1);
		projectService.registerProject(project2);
		projectService.registerProject(project3);
		projectService.registerProject(project4);
		projectService.registerProject(project5);

		assertThat(projectRepository.findAll().size()).isEqualTo(5);

	}

	@Test
	@Transactional
	void 프로젝트_수정_테스트() {
		ProjectUpdateDetailDto project1Detail = ProjectUpdateDetailDto.builder()
			.userName("강캡틴")
			.projectName("스마트 건설 TF")
			.projectDesc("(수정) 건설 현장에서 카카오톡을 이용하여 관리를 용이하게 합니다.")
			.projectTech("iOS, Android, Kotlin, MySQL, MongoDB")
			.projectStartDt(LocalDate.of(2023, 7, 8))
			.projectEndDt(LocalDate.of(2023, 9, 12))
			.build();

		projectService.updateProjectDetail(project1Detail);

		assertThat(projectRepository.findAllByProjectDescIsContaining("수정").size()).isEqualTo(1);

	}
}