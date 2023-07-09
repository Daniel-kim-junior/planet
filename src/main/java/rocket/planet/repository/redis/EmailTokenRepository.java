package rocket.planet.repository.redis;

import org.springframework.data.repository.CrudRepository;

import rocket.planet.domain.redis.EmailToken;

public interface EmailTokenRepository extends CrudRepository<EmailToken, String> {
}
