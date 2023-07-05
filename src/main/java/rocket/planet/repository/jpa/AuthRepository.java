package rocket.planet.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import rocket.planet.domain.AuthType;
import rocket.planet.domain.Authority;
public interface AuthRepository extends JpaRepository<Authority, UUID> {

    public Authority findByAuthType(AuthType authType);
}
