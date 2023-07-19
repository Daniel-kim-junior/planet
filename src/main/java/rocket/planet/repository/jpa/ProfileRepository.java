package rocket.planet.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rocket.planet.domain.Org;
import rocket.planet.domain.Profile;
import rocket.planet.repository.jpa.profile.ProfileRepositoryCustom;

public interface ProfileRepository extends JpaRepository<Profile, UUID>, ProfileRepositoryCustom {

	Optional<Profile> findByUserNickName(String userNickName);

	Profile findByOrg(Optional<Org> organization);

	List<Profile> findByUserNickNameContains(String userNickName);

	@Query(
		"select distinct p "
			+ "from Profile p "
			+ "join FETCH p.org o "
			+ "JOIN FETCH o.department d "
			+ "where d.deptName = :deptName "
			+ "and p.profileStatus = true")
	List<Profile> findCareerStatsByDepartment(String deptName);

	@Query(
		"select distinct p "
			+ "from Profile p "
			+ "join FETCH p.org o "
			+ "JOIN FETCH o.team t "
			+ "where t.teamName = :teamName "
			+ "and p.profileStatus = true")
	List<Profile> findCareerStatsByTeam(String teamName);

	@Query("select p.profileCareer from Profile p where p.profileStatus = true")
	List<Integer> findCareerStatsByEntire();

}
