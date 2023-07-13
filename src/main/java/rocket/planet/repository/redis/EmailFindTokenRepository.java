package rocket.planet.repository.redis;

import org.springframework.data.repository.CrudRepository;

import rocket.planet.domain.redis.EmailFindToken;

public interface EmailFindTokenRepository extends CrudRepository<EmailFindToken, String> {

}
