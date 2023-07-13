package rocket.planet.controller.project;

import static rocket.planet.dto.project.ProjectUpdateDto.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.domain.Project;
import rocket.planet.dto.project.ProjectCloseResDto;
import rocket.planet.dto.project.ProjectDetailResDto;
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
	public ResponseEntity<ProjectDetailResDto> projectDetail(String projectName) {

		return ResponseEntity.ok().body(projectService.getProject(projectName));
	}

	@GetMapping("/management/projects/{userNickName}")
	public ResponseEntity<Boolean> userNickName(@PathVariable("userNickName") String userNickName) {
		boolean isPresent = projectService.checkUser(userNickName);

		return ResponseEntity.ok().body(isPresent);
	}

	@PostMapping("/management/projects")
	public ResponseEntity<String> projectRegister(@RequestBody ProjectRegisterReqDto registerReqDto) {
		Project newProject = projectService.registerProject(registerReqDto);

		projectService.registerMemberToProject(registerReqDto, newProject);

		return ResponseEntity.ok().body("프로젝트 생성이 완료되었습니다.");
	}

	@PatchMapping("/management/projects")
	public ResponseEntity<String> projectDetailUpdate(String projectName, ProjectUpdateDetailDto projectDetailDto) {
		projectService.updateProjectDetail(projectDetailDto);

		return ResponseEntity.ok().body("프로젝트 수정이 완료되었습니다.");
	}

	@PatchMapping("/management/projects/disable")
	public ResponseEntity<String> projectDelete(ProjectUpdateStatusDto projectDeleteDto) {
		projectService.deleteProject(projectDeleteDto);

		return ResponseEntity.ok().body("프로젝트 삭제가 완료되었습니다.");
	}

	@PatchMapping("/management/projects/finish")
	public ResponseEntity<String> projectClose(String projectName,
		String userNickName, String role) {
		projectService.closeProject(projectName, userNickName);

		return ResponseEntity.ok().body("프로젝트를 마감하였습니다.");
	}

	@PatchMapping("/management/projects/confirm")
	public ResponseEntity<String> projectCloseApprove(String projectName,
		String userNickName, String role) {
		projectService.closeProjectApprove(projectName,
			userNickName, role);

		return ResponseEntity.ok().body("프로젝트를 마감 요청을 승인하였습니다.");
	}

	@PostMapping("/management/projects/confirm")
	public ResponseEntity<String> projectCloseRequest(@RequestBody String projectName,
		@RequestBody String userNickName) {
		projectService.requestProjectClose(projectName, userNickName);

		return ResponseEntity.ok().body("프로젝트 마감 신청이 완료되었습니다.");
	}

	@GetMapping("/management/projects")
	public ResponseEntity<List<ProjectSummaryResDto>> projectList(String teamName, String userNickName, String role) {
		List<ProjectSummaryResDto> projectList = projectService.getProjectList(teamName);

		return ResponseEntity.ok().body(projectList);
	}

	@GetMapping("/management/projects/request")
	private ResponseEntity<List<ProjectCloseResDto>> projectReqList(String teamName, String userNickName, String role) {
		List<ProjectCloseResDto> projectList = projectService.getProjecReqtList(teamName);

		return ResponseEntity.ok().body(projectList);
	}

}
