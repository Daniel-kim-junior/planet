package rocket.planet.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Org;
import rocket.planet.domain.Team;

public interface OrgRepository extends JpaRepository<Org, UUID> {

	List<Org> findAllByTeam_TeamName(String teamName);
	Optional<List<Org>> findByTeam_TeamInactive(boolean teamInactive);
	Optional<List<Org>> findByDepartment_DeptInactive(boolean deptInactive);


}
