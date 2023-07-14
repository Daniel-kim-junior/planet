package rocket.planet.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.ProfileTech;
 
public interface PfTechRepository extends JpaRepository<ProfileTech, UUID> {

    boolean existsByProfile_UserNickNameAndTech_TechName(String userName, String techName);
    List<ProfileTech> findByProfile_UserNickName(String userNickName);


}
