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

		return ResponseEntity.ok().body(projectService.getProject(projectName.getName()));
	}

	@GetMapping("/management/projects/{userNickName}")
	public ResponseEntity<Boolean> userNickNameCheck(@PathVariable("userNickName") String userNickName) {
		boolean isPresent = projectService.checkUser(userNickName);

		return ResponseEntity.ok().body(isPresent);
	}

	@PostMapping("/management/projects")
	public ResponseEntity<String> projectSave(@RequestBody ProjectRegisterReqDto registerReqDto) {
		projectService.registerProject(registerReqDto);

		return ResponseEntity.ok().body("프로젝트 생성이 완료되었습니다.");
	}

	@PatchMapping("/management/projects")
	public ResponseEntity<String> projectDetailModify(@RequestBody ProjectUpdateDetailDto projectDetailDto) {
		projectService.updateProjectDetail(projectDetailDto);

		return ResponseEntity.ok().body("프로젝트 수정이 완료되었습니다.");
	}

	@PatchMapping("/management/projects/disable")
	public ResponseEntity<String> projectRemove(@RequestBody ProjectUpdateStatusDto projectDeleteDto) {
		projectService.deleteProject(projectDeleteDto);

		return ResponseEntity.ok().body("프로젝트 삭제가 완료되었습니다.");
	}

	@PatchMapping("/management/projects/finish")
	public ResponseEntity<String> projectClose(@RequestBody ProjectNameReqDto projectNameReqDto) {
		projectService.closeProject(projectNameReqDto.getName(), projectNameReqDto.getUserNickName());

		return ResponseEntity.ok().body("프로젝트를 마감하였습니다.");
	}

	@PatchMapping("/management/projects/confirm")
	public ResponseEntity<String> projectCloseApprove(@RequestBody CloseReqDto closeReqDto) {
		String result = projectService.closeProjectApprove(closeReqDto.getName(),
			closeReqDto.getUserNickName(), closeReqDto.getRole(), closeReqDto.isApprove());

		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/management/projects/confirm")
	public ResponseEntity<String> projectCloseRequest(@RequestBody ProjectNameReqDto projectNameReqDto) {
		projectService.requestProjectClose(projectNameReqDto.getName(), projectNameReqDto.getUserNickName());

		return ResponseEntity.ok().body("프로젝트 마감 신청이 완료되었습니다.");
	}

	@PostMapping("/management/projects/list")
	public ResponseEntity<List<ProjectSummaryResDto>> projectList(@RequestBody ProjectNameReqDto projectNameReqDto) {
		List<ProjectSummaryResDto> projectList = projectService.getProjectList(projectNameReqDto.getName());

		return ResponseEntity.ok().body(projectList);
	}

	@GetMapping("/management/projects/request")
	public ResponseEntity<List<ProjectCloseResDto>> projectReqList(
		@ModelAttribute ProjectNameReqDto projectNameReqDto) {
		List<ProjectCloseResDto> projectList = projectService.getProjectReqList(projectNameReqDto.getName());

		return ResponseEntity.ok().body(projectList);
	}

}
