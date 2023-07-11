package rocket.planet.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Profile;

import rocket.planet.repository.jpa.profile.ProfileRepositoryCustom;

public interface ProfileRepository extends JpaRepository<Profile, UUID>, ProfileRepositoryCustom {

	Optional<Profile> findByUserNickName(String userNickName);
	UUID findIdByUserNickName(String userNickName);


}
