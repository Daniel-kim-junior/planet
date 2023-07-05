package rocket.planet.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findAllByUserIdContaining(String searchWord);
    Optional<User> findByUserIdContaining(String userId);

	Optional<User> findByUserName(String userName);
}
