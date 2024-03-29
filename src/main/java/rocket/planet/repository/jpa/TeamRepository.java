package rocket.planet.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rocket.planet.domain.Team;

public interface TeamRepository extends JpaRepository<Team, UUID> {

	Team findByTeamName(String teamName);

	@Query("SELECT t FROM Team t JOIN FETCH t.department d WHERE d.deptName = :deptName and t.teamInactive = false")
	List<Team> findTeamNameByDeptName(@Param("deptName") String deptName);

	@Query("SELECT t FROM Team t JOIN FETCH t.department d WHERE d.deptName = :deptName and t.teamName = :teamName and t.teamInactive = false")
	Optional<Team> findTeamByDeptName(@Param("deptName") String deptName, @Param("teamName") String teamName);

}
