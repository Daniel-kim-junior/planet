package rocket.planet.repository.redis;

import org.springframework.data.repository.CrudRepository;

import rocket.planet.domain.redis.ProfileVisitor;

public interface ProfileVisitorRepository extends CrudRepository<ProfileVisitor, String> {
}
