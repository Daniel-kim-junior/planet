package rocket.planet.repository.redis;

import org.springframework.data.repository.CrudRepository;

import rocket.planet.domain.redis.LastLogin;

public interface LastLoginRepository extends CrudRepository<LastLogin, String> {
}
