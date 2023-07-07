package rocket.planet.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Profile;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
	Profile findByUserIdContaining(String userId);

	Optional<Profile> findByUserNickName(String userNickName);
}
