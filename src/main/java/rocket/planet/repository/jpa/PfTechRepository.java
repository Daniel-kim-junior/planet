package rocket.planet.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rocket.planet.domain.ProfileTech;

public interface PfTechRepository extends JpaRepository<ProfileTech, UUID> {

	boolean existsByProfile_UserNickNameAndTech_TechName(String userNickName, String techName);

	Optional<ProfileTech> findByProfile_UserNickNameAndTech_TechName(String userNickName, String techName);

	List<ProfileTech> findByProfile_UserNickName(String userNickName);

	@Query("select distinct pt "
		+ "from ProfileTech pt "
		+ "join fetch pt.tech t "
		+ "join fetch pt.profile p "
		+ "join fetch p.org o "
		+ "join fetch o.department d "
		+ "where d.deptName = :deptName ")
	List<ProfileTech> findTechStatsByProfileDepartment(@Param("deptName") String deptName);

	@Query("select distinct pt "
		+ "from ProfileTech pt "
		+ "join fetch pt.tech th "
		+ "join fetch pt.profile p "
		+ "join fetch p.org o "
		+ "join fetch o.team t "
		+ "where t.teamName = :teamName "
		+ "and p.profileStatus = true ")
	List<ProfileTech> findTechStatsByProfileTeam(@Param("teamName") String teamName);

	@Query("select distinct pt "
		+ "from ProfileTech pt "
		+ "join fetch pt.profile pf "
		+ "join fetch pt.tech t "
		+ "where pf.profileStatus = true ")
	List<ProfileTech> findTechStatsByProfileEntire();

}
