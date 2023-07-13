package rocket.planet.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Org;

public interface OrgRepository extends JpaRepository<Org, UUID> {

	List<Org> findAllByTeam_TeamName(String teamName);
}
