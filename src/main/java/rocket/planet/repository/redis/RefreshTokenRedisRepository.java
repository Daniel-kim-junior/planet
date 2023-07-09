package rocket.planet.repository.redis;

import org.springframework.data.repository.CrudRepository;

import rocket.planet.domain.redis.RefreshToken;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
