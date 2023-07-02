package rocket.planet.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.ProfileTech;
import rocket.planet.domain.ProfileTechId;

public interface PfTechRepository extends JpaRepository<ProfileTech, ProfileTechId> {
}
