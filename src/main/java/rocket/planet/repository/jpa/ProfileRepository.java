package rocket.planet.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Profile;
<<<<<<< HEAD
 
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
	Profile findByUserIdContaining(String userId);

=======
import rocket.planet.repository.jpa.profile.ProfileRepositoryCustom;

public interface ProfileRepository extends JpaRepository<Profile, UUID>, ProfileRepositoryCustom {
>>>>>>> b99ab51 ([PN-65] feat: 프로필 상세보기)
	Optional<Profile> findByUserNickName(String userNickName);


}
