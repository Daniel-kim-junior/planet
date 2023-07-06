package rocket.planet.service.project;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rocket.planet.domain.Project;
import rocket.planet.dto.project.ProjectRegisterReqDto;
import rocket.planet.dto.project.ProjectRegisterResDto;
import rocket.planet.repository.jpa.OrgRepository;
import rocket.planet.repository.jpa.ProjectRepository;
import rocket.planet.repository.jpa.TeamRepository;
import rocket.planet.repository.jpa.UserRepository;

@Service
@RequiredArgsConstructor
public class ProjectService {
	private final ProjectRepository projectRepository;
	private final OrgRepository orgRepository;
	private final UserRepository userRepository;
	private final TeamRepository teamRepository;

	public ProjectRegisterResDto registerProject(ProjectRegisterReqDto registerDto) {
		// Optional<User> user = userRepository.findByUserName(registerDto.getUserName());
		// Optional<Org> org = orgRepository.findByUser_Id(user.get().getId());
		// Optional<Team> team = teamRepository.findById(org.get().getTeam().getId());
		//
		// Project project = Project.builder()
		// 		.projectName(registerDto.getProjectName())
		// 		.projectDesc(registerDto.getProjectDesc())
		// 		.projectTech(registerDto.getProjectTech())
		// 		.projectStartDt(registerDto.getProjectStartDt())
		// 		.projectEndDt(registerDto.getProjectEndDt())
		// 		.projectLastModifiedBy(registerDto.getUserName())
		// 		.projectStatus(ProjectStatus.WAITING)
		// 		.team(team.get())
		// 		.projectType(team.get().getTeamType())
		// 		.build();
		//
		// Project savedProject = projectRepository.save(project);
		// return project.toProjectRegisterResDto(savedProject);

		return null;
	}

	public Project getOnebyProjectName(String projectName) {
		return projectRepository.findByProjectName(projectName);
	}

	public void updateProject(Project project) {
		Optional<Project> updatedProject = projectRepository.findById(project.getId());
		updatedProject.get().update(project);

	}
}
