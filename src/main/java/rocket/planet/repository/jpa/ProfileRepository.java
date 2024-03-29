package rocket.planet.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rocket.planet.domain.Org;
import rocket.planet.domain.Profile;
import rocket.planet.repository.jpa.profile.ProfileRepositoryCustom;

public interface ProfileRepository extends JpaRepository<Profile, UUID>, ProfileRepositoryCustom {

	int countProfileBy();

	Optional<Profile> findByUserNickName(String userNickName);

	Profile findByOrg(Optional<Org> organization);

	List<Profile> findByUserNickNameStartsWith(String userNickName);

	@Query("select distinct p "
		+ "from Profile p "
		+ "where p.userNickName like %:userNickName% and p.role != 'ADMIN' and p.role != 'RADAR'")
	List<Profile> findByUserNickNameAndRole(@Param("userNickName") String userNickName);

	@Query(
		"select p "
			+ "from Profile p "
			+ "JOIN p.org o "
			+ "JOIN o.department d "
			+ "where d.deptName = :deptName "
			+ "and p.profileStatus = true")
	List<Profile> findCareerStatsByDepartment(@Param("deptName") String deptName);

	@Query(
		"select p "
			+ "from Profile p "
			+ "JOIN p.org o "
			+ "LEFT JOIN o.team t "
			+ "where t.teamName = :teamName "
			+ "and p.profileStatus = true")
	List<Profile> findCareerStatsByTeam(@Param("teamName") String teamName);

	@Query("select p.profileCareer from Profile p where p.profileStatus = true")
	List<Integer> findCareerStatsByEntire();

	List<Profile> findAllByOrg(Object o);

}
