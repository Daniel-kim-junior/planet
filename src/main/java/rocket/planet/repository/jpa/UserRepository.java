package rocket.planet.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.User;

public interface UserRepository extends JpaRepository<User, UUID> {

	Optional<User> findByUserIdContaining(String userId);

	Optional<User> findByUserId(String userId);

	User findByProfile_Id(UUID adminProfileId);
}
