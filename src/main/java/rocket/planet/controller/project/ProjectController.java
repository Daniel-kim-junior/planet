package rocket.planet.controller.project;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rocket.planet.domain.Project;
import rocket.planet.dto.common.CommonResponse;
import rocket.planet.dto.project.ProjectRegisterReqDto;
import rocket.planet.dto.project.ProjectRegisterResDto;
import rocket.planet.service.project.ProjectService;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;

	@PostMapping
	public CommonResponse<ProjectRegisterResDto> projectRegister(@RequestBody ProjectRegisterReqDto registerReqDto){
		ProjectRegisterResDto projectRegisterResDto = projectService.registerProject(registerReqDto);

		return new CommonResponse<>(true, projectRegisterResDto, null);
	}

	@PatchMapping
	public CommonResponse<String> projectUpdate(@RequestBody Project project){
		projectService.updateProject(project);
		return new CommonResponse<>(true, "update success", null);
	}

}
