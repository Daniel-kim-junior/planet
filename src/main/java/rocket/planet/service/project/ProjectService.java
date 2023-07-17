package rocket.planet.service.project;

import static rocket.planet.dto.admin.AdminDto.*;
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
import rocket.planet.domain.AuthType;
import rocket.planet.domain.Authority;
import rocket.planet.domain.OrgType;
import rocket.planet.domain.Profile;
import rocket.planet.domain.Project;
import rocket.planet.domain.ProjectStatus;
import rocket.planet.domain.Team;
import rocket.planet.domain.UserProject;
import rocket.planet.dto.project.ProjectCloseResDto;
import rocket.planet.dto.project.ProjectDetailResDto;
import rocket.planet.dto.project.ProjectRegisterReqDto;
import rocket.planet.dto.project.ProjectSummaryResDto;
import rocket.planet.repository.jpa.AuthRepository;
import rocket.planet.repository.jpa.DeptRepository;
import rocket.planet.repository.jpa.PfAuthRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.ProjectRepository;
import rocket.planet.repository.jpa.TeamRepository;
import rocket.planet.repository.jpa.UserPjtRepository;
import rocket.planet.service.auth.AuthorityService;
//import rocket.planet.service.auth.AuthorityService;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {
	private final ProjectRepository projectRepository;
	private final ProfileRepository profileRepository;
	private final UserPjtRepository userPjtRepository;
	private final AuthRepository authRepository;
	private final PfAuthRepository pfAuthRepository;
	private final TeamRepository teamRepository;
	private final DeptRepository deptRepository;

	private final AuthorityService authorityService;

	@Transactional
	public ProjectDetailResDto getProject(String projectName) {
		Optional<Project> project = projectRepository.findByProjectName(projectName);

		Authority auth = authRepository.findByAuthTargetId(project.get().getId());

		List<UserProject> userprojectList = userPjtRepository.findAllByProject(project);

		List<String> projectMember = userprojectList.stream()
			.map(user -> user.getProfile().getUserNickName())
			.collect(Collectors.toList());

		return ProjectDetailResDto.builder()
			.projectName(projectName)
			.projectLeader(
				pfAuthRepository.findByAuthority(auth).getProfile().getUserNickName())
			.team(project.get().getTeam().getTeamName())
			.dept(project.get().getTeam().getDepartment().getDeptName())
			.projectMember(projectMember)
			.projectStatus(String.valueOf(project.get().getProjectStatus()))
			.projectStartDt(project.get().getProjectStartDt())
			.projectEndDt(project.get().getProjectEndDt())
			.projectTech(project.get().getProjectTech())
			.projectDesc(project.get().getProjectDesc())
			.projectLastModifiedBy(project.get().getProjectLastModifiedBy())
			.lastModifiedDate(project.get().getLastModifiedDate())
			.build();

	}

	@Transactional
	public UserProject registerProject(ProjectRegisterReqDto registerDto) {
		Team team = teamRepository.findByTeamName(registerDto.getTeamName());
		OrgType teamType = team.getTeamType();
		ProjectStatus status = ProjectStatus.ONGOING;

		if (registerDto.getProjectStartDt().isAfter(LocalDate.now()))
			status = ProjectStatus.WAITING;

		Project project = Project.builder()
			.projectName(registerDto.getProjectName())
			.projectDesc(registerDto.getProjectDesc())
			.projectTech(registerDto.getProjectTech())
			.projectStartDt(registerDto.getProjectStartDt())
			.projectEndDt(registerDto.getProjectEndDt())
			.projectLastModifiedBy(registerDto.getUserNickName())
			.projectStatus(status)
			.team(team)
			.projectType(teamType)
			.build();

		Project newProject = projectRepository.save(project);

		return registerMemberToProject(registerDto, newProject);

	}

	@Transactional
	public UserProject registerMemberToProject(ProjectRegisterReqDto registerDto, Project project) {
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

			return userPjtRepository.save(newProject);
		}

		// Project Leader 등록
		authorityService.addAuthority(AdminAddAuthDto.builder()
			.authTargetId(project.getId())
			.authNickName(registerDto.getProjectLeader())
			.authType(AuthType.PROJECT)
			.authorizerNickName(registerDto.getUserNickName()).build()
		);

		return null;
	}

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

	@Transactional
	public List<ProjectSummaryResDto> getProjectList(String teamName) {
		List<ProjectSummaryResDto> projectSummaryList = new ArrayList<>();

		List<Project> projectsList = projectRepository.findAllByTeam_TeamName(teamName);
		for (Project project : projectsList) {
			Authority auth = authRepository.findByAuthTargetId(project.getId());
			Profile projectLeader = pfAuthRepository.findByAuthority(auth).getProfile();

			List<UserProject> userprojectList = userPjtRepository.findAllByProject(Optional.of(project));

			List<String> projectMember = userprojectList.stream()
				.map(user -> user.getProfile().getUserNickName())
				.collect(Collectors.toList());

			ProjectSummaryResDto projectSummary = ProjectSummaryResDto.builder()
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

	@Transactional
	public List<ProjectCloseResDto> getProjecReqList(String teamName) {
		List<ProjectCloseResDto> projectCloseResDto = new ArrayList<>();

		List<Project> projectsList = projectRepository.findAllByTeam_TeamName(teamName);

		for (Project project : projectsList) {

			List<UserProject> userProjectList = userPjtRepository.findAllByProject(Optional.ofNullable(project));

			Authority auth = authRepository.findByAuthTargetId(project.getId());
			Profile projectLeader = pfAuthRepository.findByAuthority(auth).getProfile();

			for (UserProject userProject : userProjectList) {
				if (userProject.isUserPjtCloseApply()) {
					ProjectCloseResDto requestedProject = ProjectCloseResDto.builder()
						.projectName(project.getProjectName())
						.projectLeader(projectLeader.getUserNickName())
						.userName(userProject.getProfile().getUserName())
						.projectStartDt(project.getProjectStartDt())
						.projectEndDt(project.getProjectEndDt())
						.build();

					projectCloseResDto.add(requestedProject);
				}
			}
		}

		return projectCloseResDto;
	}

}

