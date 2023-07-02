package rocket.planet.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Authority;

public interface AuthRepository extends JpaRepository<Authority, UUID> {
}
