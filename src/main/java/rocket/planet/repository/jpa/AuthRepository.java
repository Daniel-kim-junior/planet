package rocket.planet.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.AuthType;
import rocket.planet.domain.Authority;

public interface AuthRepository extends JpaRepository<Authority, UUID> {

	Authority findByAuthType(AuthType authType);

	List<Authority> findAllByProfileAuthority_ProfileUserId(String userId);
}
