package rocket.planet.repository.jpa.profile;

import org.springframework.data.jpa.repository.JpaRepository;
import rocket.planet.domain.Profile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfileRepositoryCustom {
    Optional<Profile> selectProfileByUserNickName(String userNickName);

}
