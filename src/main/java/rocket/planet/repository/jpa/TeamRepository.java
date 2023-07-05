package rocket.planet.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.OrgType;
import rocket.planet.domain.Team;

public interface TeamRepository extends JpaRepository<Team, UUID> {
    Team findByTeamName(String teamName);

	OrgType findTeamTypeById(UUID teamId);
}
