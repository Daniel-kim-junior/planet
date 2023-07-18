package rocket.planet.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Project;
import rocket.planet.domain.ProjectStatus;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
	List<Project> findByProjectStatusIs(ProjectStatus projectStatus);

	Optional<Project> findByProjectName(String projectName);

	List<Project> findAllByProjectDescIsContaining(String keyword);

	List<Project> findAllByProjectStatusIs(ProjectStatus projectStatus);

	UUID findIdByProjectName(String projectName);

	List<Project> findAllByTeam_TeamName(String teamName);

	List<Project> findByTeam_TeamName(String teamName);
}
