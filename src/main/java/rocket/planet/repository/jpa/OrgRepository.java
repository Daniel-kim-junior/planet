package rocket.planet.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rocket.planet.domain.Org;
import rocket.planet.domain.Team;

public interface OrgRepository extends JpaRepository<Org, UUID> {

	List<Org> findAllByTeam_TeamName(String teamName);
	Optional<List<Org>> findByTeam_TeamInactive(boolean teamInactive);
	Optional<List<Org>> findByDepartment_DeptInactive(boolean deptInactive);

	// 부문에 해당하는 팀에 해당하는 프로필의 개수를 구하는 쿼리
	@Query("SELECT o from Org o "
		+ "JOIN FETCH o.department d "
		+ "JOIN FETCH o.team t "
		+ "WHERE d.deptName = :deptName")
	List<Org> findTeamStatsByDeptName(String deptName);

	// 부문 당 프로필의 개수를 구하는 쿼리
	@Query("SELECT o from Org o "
		+ "JOIN FETCH o.department d ")
	List<Org> findDeptStatsByEntire();

	// 전체 팀에 해당하는 프로필의 개수를 구하는 쿼리
	@Query("SELECT o from Org o "
		+ "JOIN FETCH o.team t ")
	List<Org> findStatsTeamByEntire();

}
