package rocket.planet.service.project;

import static rocket.planet.dto.project.ProjectUpdateDto.*;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import rocket.planet.domain.OrgType;
import rocket.planet.domain.Profile;
import rocket.planet.domain.Project;
import rocket.planet.domain.ProjectStatus;
import rocket.planet.domain.Team;
import rocket.planet.dto.project.ProjectRegisterReqDto;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.ProjectRepository;

@Service
@RequiredArgsConstructor
public class ProjectService {
	private final ProjectRepository projectRepository;
	private final ProfileRepository profileRepository;

	@Transactional
	public void registerProject(ProjectRegisterReqDto registerDto) {
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

		projectRepository.save(project);

	}

	public ProjectUpdateDetailDto showProjectDetail(ProjectUpdateReqDto projectUpdateReqDto) {
		Optional<Project> updateProject = projectRepository.findByProjectName(projectUpdateReqDto.getProjectName());

		return ProjectUpdateDetailDto.builder()
			.userNickName(projectUpdateReqDto.getUserNickName())
			.projectName(updateProject.get().getProjectName())
			.projectDesc(updateProject.get().getProjectDesc())
			.projectTech(updateProject.get().getProjectTech())
			.projectStartDt(updateProject.get().getProjectStartDt())
			.projectEndDt(updateProject.get().getProjectEndDt())
			.build();

	}

	@Transactional
	public void updateProjectDetail(ProjectUpdateDetailDto projectUpdateDto) {
		Optional<Project> project = projectRepository.findByProjectName(projectUpdateDto.getProjectName());
		project.get().updateProject(projectUpdateDto);

	}

	public boolean checkUser(String userNickName) {
		return profileRepository.findByUserNickName(userNickName).isPresent();
	}

	// public List<ProjectSummaryDto> getProjectList(String teamName) {
	//
	// }

	// public Project getProjectDetail(String ProjectName) {
	//
	// }
}

