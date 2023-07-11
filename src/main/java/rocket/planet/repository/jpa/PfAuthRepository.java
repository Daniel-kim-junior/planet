package rocket.planet.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.ProfileAuthority;

public interface PfAuthRepository extends JpaRepository<ProfileAuthority, UUID> {
}
