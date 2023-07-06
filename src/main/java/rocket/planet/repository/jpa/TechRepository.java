package rocket.planet.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Tech;

public interface TechRepository extends JpaRepository<Tech, UUID> {
    Tech findByTechName(String techName);

}
