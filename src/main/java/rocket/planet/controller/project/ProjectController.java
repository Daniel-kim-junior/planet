package rocket.planet.controller.project;

import static rocket.planet.dto.project.ProjectUpdateDto.*;

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
import rocket.planet.domain.AuthType;
import rocket.planet.domain.Project;
import rocket.planet.dto.project.ProjectDeleteDto;
import rocket.planet.dto.project.ProjectRegisterReqDto;
import rocket.planet.service.auth.AuthorityService;
import rocket.planet.service.project.ProjectService;

@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;
	private final AuthorityService authorityService;

	@GetMapping("/projects/{userNickName}")
	public ResponseEntity<String> userNickName(@PathVariable("userNickName") String userNickName) {
		boolean isPresent = projectService.checkUser(userNickName);

		return ResponseEntity.ok()
			.body(isPresent ? userNickName + "를 팀원으로 등록하였습니다." : userNickName + "는 팀원으로 등록할 수 없습니다.");
	}

	@PostMapping("/projects")
	public ResponseEntity<String> projectRegister(@RequestBody ProjectRegisterReqDto registerReqDto) {
		if (registerReqDto.getAuthType().equals("CREW")) {
			// todo: error 코드
		}
		Project newProject = projectService.registerProject(registerReqDto);

		projectService.registerMemberToProject(registerReqDto, newProject);
		authorityService.addAuthority(newProject.getId(), AuthType.PROJECT,
			registerReqDto.getUserNickName(), registerReqDto.getProjectLeader());

		return ResponseEntity.ok().body("프로젝트 생성이 완료되었습니다.");
	}

	@PatchMapping("/projects/{projectName}")
	public ResponseEntity<String> projectDetailUpdate(@PathVariable("projectName") String projectName,
		ProjectUpdateDetailDto projectDetailDto) {
		if (projectDetailDto.getAuthType().equals("CREW")) {
			// todo: error 코드
		}
		projectService.updateProjectDetail(projectDetailDto);
		return ResponseEntity.ok().body("프로젝트 수정이 완료되었습니다.");

	}

	@PatchMapping("/projects/disable")
	public ResponseEntity<String> projectDelete(ProjectDeleteDto projectDeleteDto) {
		if (projectDeleteDto.getAuthType().equals("CREW")) {
			// todo: error code
			// todo: 담당 부문이 아닌 경우 error
		}
		projectService.deleteProject(projectDeleteDto);

		return ResponseEntity.ok().body("프로젝트 삭제가 완료되었습니다.");
	}

	// @GetMapping("/projects/{teamName}")
	// public List<ProjectSummaryDto> projectUpdate(@PathVariable("teamName") String teamName, String userNickName) {
	// 	// 프로젝트 팀 안에 속해 있는 프로젝트 리스트 불러오기
	// 	// todo: 유저 닉네임으로 팀/부문에 대한 권한 있는지 확인하기
	// 	// todo: 팀에 속한 프로젝트 리스트 불러오기
	// 	// todo: 프로젝트 summary 담아서 보내기
	//
	// 	List<ProjectSummaryDto> projectList = projectService.getProjectList(teamName);
	//
	// 	return projectList;
	// }

}
