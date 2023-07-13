package rocket.planet.repository.redis;

import org.springframework.data.repository.CrudRepository;

import rocket.planet.domain.redis.EmailJoinToken;

public interface EmailJoinTokenRepository extends CrudRepository<EmailJoinToken, String> {
}
