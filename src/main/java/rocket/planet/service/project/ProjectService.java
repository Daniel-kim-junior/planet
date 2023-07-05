package rocket.planet.service.project;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rocket.planet.domain.Project;
import rocket.planet.dto.project.ProjectRegisterReqDto;
import rocket.planet.dto.project.ProjectRegisterResDto;
import rocket.planet.repository.jpa.ProjectRepository;

@Service
@RequiredArgsConstructor
public class ProjectService {
	private final ProjectRepository projectRepository;

	public ProjectRegisterResDto registerProject(ProjectRegisterReqDto registerDto){
		Project project = Project.builder()
				.projectName(registerDto.getProjectName())
				.projectDesc(registerDto.getProjectDesc())
				.projectTech(registerDto.getProjectTech())
				.projectStartDt(registerDto.getProjectStartDt())
				.projectEndDt(registerDto.getProjectEndDt())
				.projectLastModifiedBy(registerDto.getProjectLastModifiedBy())
				.build();

		Project savedProject = projectRepository.save(project);
		return project.toProjectRegisterResDto(savedProject);
	}
	public Project getOnebyProjectName(String projectName){
		return projectRepository.findByProjectName(projectName);
	}

	public void updateProject(Project project){
		Project updatedProject = projectRepository.findById(project.getId())
			.orElseThrow();
		updatedProject.update(project);

	}
}
