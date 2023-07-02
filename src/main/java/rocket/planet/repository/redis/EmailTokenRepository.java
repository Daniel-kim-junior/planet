package rocket.planet.repository.redis;

import org.springframework.data.repository.CrudRepository;

import rocket.planet.redis.email.EmailToken;

public interface EmailTokenRepository extends CrudRepository<EmailToken, Long> {
}
