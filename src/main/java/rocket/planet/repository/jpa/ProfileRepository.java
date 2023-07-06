package rocket.planet.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Org;
import rocket.planet.domain.Profile;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
	Profile findByUserIdContaining(String userId);

	List<Org> findOrgByUserName(String userName);
}
