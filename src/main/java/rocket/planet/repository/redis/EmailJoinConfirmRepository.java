package rocket.planet.repository.redis;

import org.springframework.data.repository.CrudRepository;

import rocket.planet.domain.redis.EmailJoinConfirm;

public interface EmailJoinConfirmRepository extends CrudRepository<EmailJoinConfirm, String> {
}
