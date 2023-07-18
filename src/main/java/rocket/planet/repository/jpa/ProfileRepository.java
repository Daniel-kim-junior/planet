package rocket.planet.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rocket.planet.domain.Org;
import rocket.planet.domain.Profile;
import rocket.planet.repository.jpa.profile.ProfileRepositoryCustom;

public interface ProfileRepository extends JpaRepository<Profile, UUID>, ProfileRepositoryCustom {

	Optional<Profile> findByUserNickName(String userNickName);

	List<Profile> findByOrg(Optional<Org> organization);

	@Query(
		"select distinct p "
			+ "from Profile p "
			+ "join FETCH p.org o "
			+ "JOIN FETCH o.department d "
			+ "where d.deptName = :deptName")
	List<Profile> findCareerStatsByDepartment(String deptName);


}
