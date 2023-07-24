package rocket.planet.repository.jpa.profile;

import java.util.List;
import java.util.Optional;

import rocket.planet.domain.Profile;

public interface ProfileRepositoryCustom {
	Optional<Profile> selectProfileByUserNickName(String userNickName);

	List<Profile> selectProfilesBySearchKeyword(String keyword);
}
