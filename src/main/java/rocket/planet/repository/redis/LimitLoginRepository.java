package rocket.planet.repository.redis;

import org.springframework.data.repository.CrudRepository;

import rocket.planet.domain.redis.LimitLogin;

public interface LimitLoginRepository extends CrudRepository<LimitLogin, String> {
}
