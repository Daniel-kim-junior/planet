package rocket.planet.controller.project;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.dto.common.CommonResponse;
import rocket.planet.dto.project.ProjectRegisterReqDto;
import rocket.planet.dto.project.ProjectRegisterResDto;
import rocket.planet.dto.project.ProjectUpdateReqDto;
import rocket.planet.service.project.ProjectService;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;

	@PostMapping("/projects")
	public CommonResponse<ProjectRegisterResDto> projectRegister(@RequestBody ProjectRegisterReqDto registerReqDto) {
		ProjectRegisterResDto project = projectService.registerProject(registerReqDto);

		return new CommonResponse<>(true, project, null);
	}

	@PatchMapping("/projects")
	public CommonResponse<ProjectRegisterResDto> projectUpdate(@RequestBody ProjectUpdateReqDto project) {
		ProjectRegisterResDto updatedProject = projectService.updateProject(project);
		return new CommonResponse<>(true, updatedProject, null);
	}

}
