package rocket.planet.repository.redis;

import org.springframework.data.repository.CrudRepository;

import rocket.planet.domain.redis.AccessToken;

public interface AccessTokenRedisRepository extends CrudRepository<AccessToken, String> {
	void deleteById(String id);
}
