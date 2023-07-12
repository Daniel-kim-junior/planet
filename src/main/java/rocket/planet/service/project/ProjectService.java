package rocket.planet.service.project;

import static rocket.planet.dto.project.ProjectUpdateDto.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.domain.Authority;
import rocket.planet.domain.OrgType;
import rocket.planet.domain.Profile;
import rocket.planet.domain.Project;
import rocket.planet.domain.ProjectStatus;
import rocket.planet.domain.Team;
import rocket.planet.domain.UserProject;
import rocket.planet.dto.project.ProjectRegisterReqDto;
import rocket.planet.dto.project.ProjectSummaryDto;
import rocket.planet.repository.jpa.AuthRepository;
import rocket.planet.repository.jpa.PfAuthRepository;
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
	private final AuthRepository authRepository;
	private final PfAuthRepository pfAuthRepository;

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
	public void deleteProject(ProjectUpdateStatusDto projectDeleteDto) {
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

	public boolean isInProject(String projectName, String userNickName) {
		List<UserProject> projectList = userPjtRepository.findAllByProfile_userNickName(
			userNickName);
		return projectList.stream().anyMatch(project -> project.getProject().getProjectName().equals(projectName));

	}

	@Transactional
	public void closeProjectApprove(String projectName,
		String userNickName, String role) {
		// todo: error 처리 -> 권한 확인

		List<UserProject> userProject = userPjtRepository.findAllByProject(
			projectRepository.findByProjectName(projectName));
		Optional<Profile> profile = profileRepository.findByUserNickName(userNickName);

		// 프로젝트 목록에서 사용자가 마감 요청한 프로젝트가 있는 row를 찾아서 변경
		for (UserProject updateProject : userProject) {
			if (updateProject.getProfile().getId().equals(profile.get().getId())) {
				updateProject.toUserProjectCloseApprove();
			}
		}
	}

	@Transactional
	public void requestProjectClose(String projectName, String userNickName) {
		if (isInProject(projectName, userNickName)) {
			UserProject newUserProject = userPjtRepository.findByProject_projectNameAndProfile_userNickName(projectName,
				userNickName);
			log.info("newUserProject => {}", newUserProject);
			newUserProject.requestClose();
		} else {
			log.info("isInProject => {}", false);
		}
	}

	public List<ProjectSummaryDto> getProjectList(String teamName) {
		List<ProjectSummaryDto> projectSummaryList = new ArrayList<>();

		// 팀이름으로 프로젝트 이름 리스트 확인
		// 프로젝트 이름으로 서머리 불러오기
		List<Project> projectsList = projectRepository.findAllByTeam_TeamName(teamName);
		for (Project project : projectsList) {
			Authority auth = authRepository.findByAuthTargetId(project.getId());
			Profile projectLeader = pfAuthRepository.findByAuthority(auth).getProfile();

			List<UserProject> userprojectList = userPjtRepository.findAllByProject(Optional.of(project));

			List<String> projectMember = userprojectList.stream()
				.map(user -> user.getProfile().getUserNickName())
				.collect(Collectors.toList());

			ProjectSummaryDto projectSummary = ProjectSummaryDto.builder()
				.projectName(project.getProjectName())
				.projectLeader(projectLeader.getUserNickName())
				.projectMember(projectMember)
				.projectStartDt(project.getProjectStartDt())
				.projectEndDt(project.getProjectEndDt())
				.projectStatus(String.valueOf(project.getProjectStatus()))
				.build();

			projectSummaryList.add(projectSummary);
		}

		return projectSummaryList;
	}

	// public Project getProjectDetail(String ProjectName) {
	//
	// }
}

