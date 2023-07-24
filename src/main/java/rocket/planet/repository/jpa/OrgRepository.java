package rocket.planet.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rocket.planet.domain.Org;

public interface OrgRepository extends JpaRepository<Org, UUID> {

	List<Org> findAllByTeam_TeamName(String teamName);

	Optional<List<Org>> findByTeam_TeamInactive(boolean teamInactive);

	Optional<List<Org>> findByDepartment_DeptInactive(boolean deptInactive);

	@Query("SELECT distinct o from Org o "
		+ "JOIN FETCH o.profile pf "
		+ "LEFT JOIN FETCH o.department d "
		+ "LEFT JOIN FETCH o.team t "
		+ "WHERE d.deptName = :deptName "
		+ "and t.teamInactive = false "
		+ "and pf.profileStatus = true")
	List<Org> findTeamStatsByDeptName(@Param("deptName") String deptName);

	@Query("SELECT distinct o from Org o "
		+ "JOIN FETCH o.profile pf "
		+ "LEFT JOIN FETCH o.department d "
		+ "WHERE d.deptInactive = false "
		+ "and pf.profileStatus = true ")
	List<Org> findDeptStatsByEntire();

	@Query("SELECT distinct o from Org o "
		+ "JOIN FETCH o.profile pf "
		+ "JOIN FETCH o.team t "
		+ "WHERE t.teamInactive = false "
		+ "and pf.profileStatus = true ")
	List<Org> findStatsTeamByEntire();

}
