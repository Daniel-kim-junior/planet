package rocket.planet.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Org;

public interface OrgRepository extends JpaRepository<Org, UUID> {

}
