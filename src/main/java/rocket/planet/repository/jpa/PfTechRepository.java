package rocket.planet.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rocket.planet.domain.ProfileTech;

public interface PfTechRepository extends JpaRepository<ProfileTech, UUID> {

	boolean existsByProfile_UserNickNameAndTech_TechName(String userName, String techName);

	List<ProfileTech> findByProfile_UserNickName(String userNickName);

	@Query("select distinct pt "
		+ "from ProfileTech pt "
		+ "join fetch pt.tech t "
		+ "join fetch pt.profile p "
		+ "join fetch p.org o "
		+ "join fetch o.department d "
		+ "where d.deptName = :deptName ")
	List<ProfileTech> findTechStatsByProfileDepartment(String deptName);
}
