package rocket.planet.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Authority;
import rocket.planet.domain.Profile;
import rocket.planet.domain.ProfileAuthority;

public interface PfAuthRepository extends JpaRepository<ProfileAuthority, UUID> {

	Profile findProfileByAuthority(Authority auth);

	ProfileAuthority findByAuthority(Authority byAuthTargetId);
}
