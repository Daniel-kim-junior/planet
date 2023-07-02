package rocket.planet.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
}
