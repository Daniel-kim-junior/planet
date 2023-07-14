package rocket.planet.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Authority;
import rocket.planet.domain.Org;
import rocket.planet.domain.Profile;
import rocket.planet.domain.ProfileAuthority;
import rocket.planet.repository.jpa.profile.ProfileRepositoryCustom;

public interface ProfileRepository extends JpaRepository<Profile, UUID>, ProfileRepositoryCustom {

	Optional<Profile> findByUserNickName(String userNickName);

	UUID findIdByUserId(String userId);

	List<Profile> findByOrg(Optional<Org> organization);

//	ProfileAuthority findByAuthorityAndProfile(Authority byProfile, Profile user);

//	void deleteByAuthorityAndProfile(Authority byProfile, Profile user);
}
