package rocket.planet.repository.redis;

import org.springframework.data.repository.CrudRepository;

import rocket.planet.domain.redis.AuthChange;

public interface AuthChangeRepository extends CrudRepository<AuthChange, String> {
}
