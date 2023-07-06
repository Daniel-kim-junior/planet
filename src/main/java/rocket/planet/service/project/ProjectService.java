package rocket.planet.service.project;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import rocket.planet.domain.Org;
import rocket.planet.domain.OrgType;
import rocket.planet.domain.Project;
import rocket.planet.domain.ProjectStatus;
import rocket.planet.domain.Team;
import rocket.planet.dto.project.ProjectRegisterReqDto;
import rocket.planet.dto.project.ProjectRegisterResDto;
import rocket.planet.dto.project.ProjectUpdateReqDto;
import rocket.planet.repository.jpa.ProfileRepository;
import rocket.planet.repository.jpa.ProjectRepository;

@Service
@RequiredArgsConstructor
public class ProjectService {
	private final ProjectRepository projectRepository;
	private final ProfileRepository profileRepository;

	public ProjectRegisterResDto registerProject(ProjectRegisterReqDto registerDto) {
		List<Org> org = profileRepository.findOrgByUserName(registerDto.getUserName());
		Team team = org.get(0).getTeam();
		OrgType teamType = team.getTeamType();

		Project project = Project.builder()
			.projectName(registerDto.getProjectName())
			.projectDesc(registerDto.getProjectDesc())
			.projectTech(registerDto.getProjectTech())
			.projectStartDt(registerDto.getProjectStartDt())
			.projectEndDt(registerDto.getProjectEndDt())
			.projectLastModifiedBy(registerDto.getUserName())
			.projectStatus(ProjectStatus.WAITING)
			.team(team)
			.projectLastModifiedBy(registerDto.getUserName())
			.projectType(teamType)
			.build();

		Project savedProject = projectRepository.save(project);
		return project.toProjectRegisterResDto(savedProject);

	}

	@Transactional
	public ProjectRegisterResDto updateProject(ProjectUpdateReqDto project) {
		Optional<Project> updatedProject = projectRepository.findByProjectName(project.getProjectName());

		updatedProject.get().updateProject(project);

		return ProjectRegisterResDto.builder()
			.projectName(updatedProject.get().getProjectName())
			.projectStatus(updatedProject.get().getProjectStatus())
			.build();

	}
}
