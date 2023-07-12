package rocket.planet.repository.redis;

import org.springframework.data.repository.CrudRepository;

import rocket.planet.domain.redis.ProfileVisitant;

public interface ProfileVisitantRepository extends CrudRepository<ProfileVisitant, String> {
}
