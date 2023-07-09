package rocket.planet.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Team;

public interface TeamRepository extends JpaRepository<Team, UUID> {

    Team findByTeamName(String teamName);

	List<String> findTeamNameBy();

}
