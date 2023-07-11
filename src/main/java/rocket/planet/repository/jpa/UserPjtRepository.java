package rocket.planet.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.UserProject;

public interface UserPjtRepository extends JpaRepository<UserProject, UUID> {
	List<UserProject> findAllByProject_Id(UUID requestedProject);
}
