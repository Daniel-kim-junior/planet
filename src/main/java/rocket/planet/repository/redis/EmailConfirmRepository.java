package rocket.planet.repository.redis;

import org.springframework.data.repository.CrudRepository;

import rocket.planet.domain.redis.EmailConfirm;

public interface EmailConfirmRepository extends CrudRepository<EmailConfirm, String> {
}
