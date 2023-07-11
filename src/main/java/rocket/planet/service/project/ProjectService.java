package rocket.planet.service.project;

import static rocket.planet.dto.project.ProjectUpdateDto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.domain.OrgType;
import rocket.planet.domain.Profile;
import rocket.planet.domain.Project;
import rocket.planet.domain.ProjectStatus;
import rocket.planet.domain.Team;
import rocket.planet.domain.UserProject;
import rocket.planet.dto.project.ProjectDeleteDto;
import rocket.planet.dto.project.ProjectRegisterReqDto;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.ProjectRepository;
import rocket.planet.repository.jpa.UserPjtRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {
	private final ProjectRepository projectRepository;
	private final ProfileRepository profileRepository;
	private final UserPjtRepository userPjtRepository;

	// 프로젝트 생성
	@Transactional
	public Project registerProject(ProjectRegisterReqDto registerDto) {
		Optional<Profile> profile = profileRepository.findByUserNickName(registerDto.getUserNickName());
		Team team = profile.get().getOrg().get(0).getTeam();
		OrgType teamType = team.getTeamType();

		Project project = Project.builder()
			.projectName(registerDto.getProjectName())
			.projectDesc(registerDto.getProjectDesc())
			.projectTech(registerDto.getProjectTech())
			.projectStartDt(registerDto.getProjectStartDt())
			.projectEndDt(registerDto.getProjectEndDt())
			.projectLastModifiedBy(registerDto.getUserNickName())
			.projectStatus(ProjectStatus.WAITING)
			.team(team)
			.projectType(teamType)
			.build();

		return projectRepository.save(project);

	}

	@Transactional
	public void registerMemberToProject(ProjectRegisterReqDto registerDto, Project project) {
		List<String> membersList = registerDto.getProjectMember();

		for (String member : membersList) {
			UserProject newProject = UserProject.builder()
				.profile(profileRepository.findByUserNickName(member).get())
				.project(project)
				.userPjtInviter(registerDto.getUserNickName())
				.userPjtCloseDt(LocalDate.of(2999, 12, 31))
				.userPjtCloseApply(false)
				.userPjtDesc("")
				.build();
			userPjtRepository.save(newProject);
		}

	}

	// 프로젝트 조회
	// public ProjectUpdateDetailDto showProjectDetail(ProjectUpdateReqDto projectUpdateReqDto) {
	// 	Optional<Project> updateProject = projectRepository.findByProjectName(projectUpdateReqDto.getProjectName());
	//
	// 	return ProjectUpdateDetailDto.builder()
	// 		.userNickName(projectUpdateReqDto.getUserNickName())
	// 		.projectName(updateProject.get().getProjectName())
	// 		.projectDesc(updateProject.get().getProjectDesc())
	// 		.projectTech(updateProject.get().getProjectTech())
	// 		.projectStartDt(updateProject.get().getProjectStartDt())
	// 		.projectEndDt(updateProject.get().getProjectEndDt())
	// 		.build();
	//
	// }

	@Transactional
	public void updateProjectDetail(ProjectUpdateDetailDto projectUpdateDto) {
		Optional<Project> project = projectRepository.findByProjectName(projectUpdateDto.getProjectName());
		project.get().updateProject(projectUpdateDto);

	}

	public boolean checkUser(String userNickName) {
		return profileRepository.findByUserNickName(userNickName).isPresent();
	}

	@Transactional
	public void deleteProject(ProjectDeleteDto projectDeleteDto) {
		Optional<Project> project = projectRepository.findByProjectName(projectDeleteDto.getProjectName());
		project.get().deleteProject(projectDeleteDto);

	}

	@Transactional
	public void closeProject(String projectName, String userNickName) {
		Project requestedProject = projectRepository.findByProjectName(projectName).get();
		List<UserProject> userProjects = userPjtRepository.findAllByProject_Id(requestedProject.getId());

		// 프로젝트에 대한 마감 요청이 있다면 요청 수락으로 변경
		userProjects.stream().filter(UserProject::isUserPjtCloseApply).forEach(UserProject::toUserProjectCloseApprove);
		// 프로젝트 상태 변경
		requestedProject.close(userNickName);

	}

	// public List<ProjectSummaryDto> getProjectList(String teamName) {
	//
	// }

	// public Project getProjectDetail(String ProjectName) {
	//
	// }
}

