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
}
