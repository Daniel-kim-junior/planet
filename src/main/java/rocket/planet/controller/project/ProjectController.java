package rocket.planet.controller.project;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rocket.planet.dto.common.CommonResponse;
import rocket.planet.dto.project.ProjectRegisterReqDto;
import rocket.planet.dto.project.ProjectRegisterResDto;
import rocket.planet.service.project.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {

	private ProjectService projectService;

	@PostMapping()
	public CommonResponse<ProjectRegisterResDto> projectRegister(@RequestBody ProjectRegisterReqDto registerReqDto){
		ProjectRegisterResDto projectRegisterResDto = projectService.registerProject(registerReqDto);

		return new CommonResponse<>(true, projectRegisterResDto, null);
	}

}
