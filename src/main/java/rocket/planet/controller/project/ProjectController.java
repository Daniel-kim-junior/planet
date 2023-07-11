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

	@GetMapping("/projects/{userNickName}")
	public String userNickName(@PathVariable("userNickName") String userNickName) {
		boolean isPresent = projectService.checkUser(userNickName);

		String result = isPresent ? userNickName + "를 팀원으로 등록하였습니다." : userNickName + "는 팀원으로 등록할 수 없습니다.";
		return result;
	}

	@PostMapping("/projects")
	public String projectRegister(@RequestBody ProjectRegisterReqDto registerReqDto) {
		projectService.registerProject(registerReqDto);

		return "프로젝트 생성이 완료되었습니다.";
	}

	@PatchMapping("/projects/{projectName}")
	public String projectDetailUpdate(@PathVariable("projectName") String projectName,
		ProjectUpdateDetailDto projectDetailDto) {
		projectService.updateProjectDetail(projectDetailDto);
		return "프로젝트 생성이 완료되었습니다.";

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

	//팀

}
