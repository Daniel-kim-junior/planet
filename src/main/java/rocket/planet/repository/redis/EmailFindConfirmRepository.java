package rocket.planet.repository.redis;

import org.springframework.data.repository.CrudRepository;

import rocket.planet.domain.redis.EmailFindConfirm;

public interface EmailFindConfirmRepository extends CrudRepository<EmailFindConfirm, String> {
}
