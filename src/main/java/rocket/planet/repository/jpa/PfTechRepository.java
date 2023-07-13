package rocket.planet.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.ProfileTech;
 
public interface PfTechRepository extends JpaRepository<ProfileTech, UUID> {
    long deleteByTech_TechNameAndProfile_UserNickName(String userNickName, String techName);
}
