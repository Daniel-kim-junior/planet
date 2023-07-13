package rocket.planet.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Tech;

public interface TechRepository extends JpaRepository<Tech, UUID> {
    Tech findByTechName(String techName);
    Optional<Tech> findByTechNameIgnoreCase(String techName);

}
