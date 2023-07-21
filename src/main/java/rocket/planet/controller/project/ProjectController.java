package rocket.planet.controller.project;

import static rocket.planet.dto.project.ProjectDto.*;
import static rocket.planet.dto.project.ProjectUpdateDto.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.dto.common.CommonResDto;
import rocket.planet.dto.project.ProjectCloseResDto;
import rocket.planet.dto.project.ProjectDetailResDto;
import rocket.planet.dto.project.ProjectNameReqDto;
import rocket.planet.dto.project.ProjectRegisterReqDto;
import rocket.planet.dto.project.ProjectSummaryResDto;
import rocket.planet.service.project.ProjectService;

@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;

	@GetMapping("/project")
	public ResponseEntity<ProjectDetailResDto> projectDetails(@ModelAttribute NameReqDto projectName) {

		return ResponseEntity.ok().body(projectService.getProject(projectName));
	}

	@GetMapping("/management/projects/{userNickName}")
	public ResponseEntity<Boolean> userNickNameCheck(@PathVariable("userNickName") String userNickName) {

		return ResponseEntity.ok().body(projectService.checkUser(userNickName));
	}

	@PostMapping("/management/projects")
	public ResponseEntity<CommonResDto> projectSave(@RequestBody ProjectRegisterReqDto registerReqDto) {

		return ResponseEntity.ok().body(projectService.saveProject(registerReqDto));
	}

	@PatchMapping("/management/projects")
	public ResponseEntity<CommonResDto> projectDetailModify(@RequestBody ProjectUpdateDetailDto projectDetailDto) {

		return ResponseEntity.ok().body(projectService.updateProjectDetail(projectDetailDto));
	}

	@PatchMapping("/management/projects/disable")
	public ResponseEntity<CommonResDto> projectRemove(@RequestBody ProjectUpdateStatusDto projectDeleteDto) {

		return ResponseEntity.ok().body(projectService.deleteProject(projectDeleteDto));
	}

	@PatchMapping("/management/projects/finish")
	public ResponseEntity<CommonResDto> projectClose(@RequestBody ProjectNameReqDto projectNameReqDto) {

		return ResponseEntity.ok()
			.body(projectService.closeProject(projectNameReqDto.getName(), projectNameReqDto.getUserNickName()));
	}

	@PatchMapping("/management/projects/confirm")
	public ResponseEntity<CommonResDto> projectCloseApprove(@RequestBody CloseReqDto closeReqDto) {

		return ResponseEntity.ok().body(projectService.closeProjectApprove(closeReqDto));
	}

	@PostMapping("/management/projects/confirm")
	public ResponseEntity<CommonResDto> projectCloseRequest(@RequestBody ProjectNameReqDto projectNameReqDto) {

		return ResponseEntity.ok()
			.body(projectService.requestProjectClose(projectNameReqDto));
	}

	@PostMapping("/management/projects/list")
	public ResponseEntity<List<ProjectSummaryResDto>> projectList(@RequestBody ProjectNameReqDto projectNameReqDto) {
		List<ProjectSummaryResDto> projectList = projectService.getProjectList(projectNameReqDto);

		return ResponseEntity.ok().body(projectList);
	}

	@GetMapping("/management/projects/request")
	public ResponseEntity<List<ProjectCloseResDto>> projectReqList(
		@ModelAttribute ProjectNameReqDto projectNameReqDto) {
		List<ProjectCloseResDto> projectList = projectService.getProjectReqList(projectNameReqDto);

		return ResponseEntity.ok().body(projectList);
	}

}
