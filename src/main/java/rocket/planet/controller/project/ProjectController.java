package rocket.planet.controller.project;

import static rocket.planet.dto.project.ProjectUpdateDto.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.dto.project.ProjectRegisterReqDto;
import rocket.planet.service.project.ProjectService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;

	@PostMapping("/projects")
	public String projectRegister(@RequestBody ProjectRegisterReqDto registerReqDto) {
		projectService.registerProject(registerReqDto);

		return "프로젝트 생성이 완료되었습니다.";
	}

	@PatchMapping("/projects/{projectName}")
	public String projectDetailUpdate(@PathVariable("projectName") String projectName,
		ProjectUpdateDetailDto projectDetailDto) {
		projectService.updateProjectDetail(projectDetailDto);
		return "프로젝트 수정이 완료되었습니다.";

	}

	@GetMapping("/projects/{teamName}")
	public ProjectUpdateDetailDto projectUpdate(@PathVariable("teamName") String teamName,
		ProjectUpdateReqDto projectUpdateDto) {
		// 프로젝트 팀 안에 속해 있는 리스트 불러오기

		ProjectUpdateDetailDto selectedProject = projectService.showProjectDetail(projectUpdateDto);
		return selectedProject;
	}

	//팀

}
