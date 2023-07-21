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
import rocket.planet.dto.common.CommonResDto;
import rocket.planet.dto.project.ProjectCloseResDto;
import rocket.planet.dto.project.ProjectDetailResDto;
import rocket.planet.dto.project.ProjectDto.CloseReqDto;
import rocket.planet.dto.project.ProjectDto.NameReqDto;
import rocket.planet.dto.project.ProjectNameReqDto;
import rocket.planet.dto.project.ProjectRegisterReqDto;
import rocket.planet.dto.project.ProjectSummaryResDto;
import rocket.planet.repository.jpa.AuthRepository;
import rocket.planet.repository.jpa.PfAuthRepository;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.ProjectRepository;
import rocket.planet.repository.jpa.TeamRepository;
import rocket.planet.repository.jpa.UserPjtRepository;
import rocket.planet.service.auth.AuthorityService;
import rocket.planet.util.exception.ReqNotFoundException;

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

	private final AuthorityService authorityService;

	@Transactional
	public ProjectDetailResDto getProject(NameReqDto projectName) {
		Project project = projectRepository.findByProjectName(projectName.getName())
			.orElseThrow(() -> new ReqNotFoundException("해당하는 프로젝트가 존재하지 않습니다."));

		Authority auth = authRepository.findByAuthTargetId(project.getId());

		List<UserProject> userprojectList = userPjtRepository.findAllByProject(Optional.of(project));

		List<String> projectMember = userprojectList.stream()
			.map(user -> user.getProfile().getUserNickName())
			.collect(Collectors.toList());

		return ProjectDetailResDto.builder()
			.projectName(projectName.getName())
			.projectLeader(
				pfAuthRepository.findByAuthority(auth).getProfile().getUserNickName())
			.team(project.getTeam().getTeamName())
			.dept(project.getTeam().getDepartment().getDeptName())
			.projectMember(projectMember)
			.projectStatus(String.valueOf(project.getProjectStatus()))
			.projectStartDt(project.getProjectStartDt())
			.projectEndDt(project.getProjectEndDt())
			.projectTech(project.getProjectTech())
			.projectDesc(project.getProjectDesc())
			.projectLastModifiedBy(project.getProjectLastModifiedBy())
			.lastModifiedDate(project.getLastModifiedDt())
			.build();

	}

	@Transactional
	public CommonResDto saveProject(ProjectRegisterReqDto registerDto) {

		if (projectRepository.findByProjectName(registerDto.getProjectName()).isPresent()) {
			throw new ReqNotFoundException("동일한 프로젝트 이름이 존재합니다.");
		}

		Team team = Optional.ofNullable(teamRepository.findByTeamName(registerDto.getTeamName()))
			.orElseThrow(() -> new ReqNotFoundException("해당하는 팀 이름이 존재하지 않습니다."));
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

		addMemberToProject(registerDto, newProject);

		return CommonResDto.builder().message("프로젝트 생성이 완료되었습니다.").build();
	}

	@Transactional
	public void addMemberToProject(ProjectRegisterReqDto registerDto, Project project) {
		List<String> membersList = Optional.ofNullable(registerDto.getProjectMember())
			.orElseThrow(() -> new ReqNotFoundException("팀원이 존재하지 않습니다."));

		membersList.stream().map(member -> UserProject.builder()
			.profile(profileRepository.findByUserNickName(member).get())
			.project(project)
			.userPjtInviter(registerDto.getUserNickName())
			.userPjtCloseDt(LocalDate.of(2999, 12, 31))
			.userPjtCloseApply(false)
			.userPjtDesc("")
			.build()).forEach(userPjtRepository::save);

		// Project Leader 등록
		authorityService.addAuthority(AdminAddAuthDto.builder()
			.authTargetId(project.getId())
			.authNickName(registerDto.getProjectLeader())
			.authType(AuthType.PROJECT)
			.authorizerNickName(registerDto.getUserNickName()).build()
		);

	}

	@Transactional
	public CommonResDto updateProjectDetail(ProjectUpdateDetailDto projectUpdateDto) {
		Project project = projectRepository.findByProjectName(projectUpdateDto.getProjectName())
			.orElseThrow(() -> new ReqNotFoundException("해당하는 프로젝트가 존재하지 않습니다."));

		// 프로젝트 리더인 경우에만 수정 가능
		if (!projectUpdateDto.getProjectLeader().equals(projectUpdateDto.getUserNickName())) {
			throw new ReqNotFoundException("해당 프로젝트의 리더인 경우에만 수정이 가능합니다.");
		}

		project.updateProject(projectUpdateDto);

		return CommonResDto.builder().message("해당 프로젝트 수정이 완료되었습니다.").build();
	}

	public boolean checkUser(String userNickName) {
		return profileRepository.findByUserNickName(userNickName).isPresent();
	}

	@Transactional
	public CommonResDto deleteProject(ProjectUpdateStatusDto projectDeleteDto) {
		Project project = projectRepository.findByProjectName(projectDeleteDto.getProjectName())
			.orElseThrow(() -> new ReqNotFoundException("해당하는 프로젝트가 존재하지 않습니다."));
		project.deleteProject(projectDeleteDto);

		return CommonResDto.builder().message("해당 프로젝트 삭제가 완료되었습니다.").build();

	}

	@Transactional
	public CommonResDto closeProject(String projectName, String userNickName) {
		Project requestedProject = projectRepository.findByProjectName(projectName)
			.orElseThrow(() -> new ReqNotFoundException("해당하는 프로젝트가 존재하지 않습니다."));
		List<UserProject> userProjects = userPjtRepository.findAllByProject_Id(requestedProject.getId());

		// 프로젝트-유저 변경
		userProjects.forEach(UserProject::approveProjectClose);

		// 프로젝트 상태 변경
		requestedProject.close(userNickName);

		return CommonResDto.builder().message("해당 프로젝트를 마감으로 변경하였습니다.").build();
	}

	public boolean isInProject(String projectName, String userNickName) {
		List<UserProject> projectList = userPjtRepository.findAllByProfile_userNickName(
			userNickName);
		return projectList.stream().anyMatch(project -> project.getProject().getProjectName().equals(projectName));

	}

	@Transactional
	public CommonResDto closeProjectApprove(CloseReqDto closeReqDto) {

		UserProject requestedProject = userPjtRepository.findByProject_projectNameAndProfile_userNickName(
			closeReqDto.getName(),
			closeReqDto.getUserNickName());

		if (closeReqDto.getIsApprove().equals("true")) {
			requestedProject.approveProjectClose();
			return CommonResDto.builder().message("마감 요청을 승인하였습니다.").build();

		} else {
			requestedProject.rejectProjectClose();
			return CommonResDto.builder().message("마감 요청을 반려하였습니다.").build();

		}
	}

	@Transactional
	public CommonResDto requestProjectClose(ProjectNameReqDto projectNameReqDto) {
		if (isInProject(projectNameReqDto.getName(), projectNameReqDto.getUserNickName())) {
			UserProject newUserProject = userPjtRepository.findByProject_projectNameAndProfile_userNickName(
				projectNameReqDto.getName(),
				projectNameReqDto.getUserNickName());
			newUserProject.requestProjectClose();
		} else {
			throw new ReqNotFoundException("해당 프로젝트의 팀원만 완수 요청을 할 수 있습니다.");
		}
		return CommonResDto.builder().message("프로젝트 마감 요청을 완료하였습니다.").build();
	}

	@Transactional
	public List<ProjectSummaryResDto> getProjectList(ProjectNameReqDto projectNameReqDto) {
		List<ProjectSummaryResDto> projectSummaryList = new ArrayList<>();

		List<Project> projectsList = projectRepository.findAllByTeam_TeamName(projectNameReqDto.getName());
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
	public List<ProjectCloseResDto> getProjectReqList(ProjectNameReqDto projectNameReqDto) {
		// 팀이름으로 마감 요청 리스트 조회
		List<ProjectCloseResDto> projectCloseResDto = new ArrayList<>();

		// 팀이름으로 프로젝트 리스트 찾기
		List<Project> projectsList = projectRepository.findByTeam_TeamName(projectNameReqDto.getName());

		if (!projectsList.isEmpty()) {
			for (Project project : projectsList) {
				// 프로젝트로 프로젝트-유저 정보 찾기
				List<UserProject> userProjectList = userPjtRepository.findAllByProject(Optional.ofNullable(project));

				// 프로젝트 리더 찾기
				Authority auth = authRepository.findByAuthTargetId(project.getId());
				Profile projectLeader = pfAuthRepository.findByAuthority(auth).getProfile();

				for (UserProject userProject : userProjectList) {
					if (userProject.isUserPjtCloseApply()) {
						ProjectCloseResDto requestedProject = ProjectCloseResDto.builder()
							.projectName(project.getProjectName())
							.projectLeader(projectLeader.getUserNickName())
							.userNickName(userProject.getProfile().getUserNickName())
							.projectStartDt(project.getProjectStartDt())
							.projectEndDt(project.getProjectEndDt())
							.build();

						projectCloseResDto.add(requestedProject);
					}
				}
			}
		}

		return projectCloseResDto;
	}

}

