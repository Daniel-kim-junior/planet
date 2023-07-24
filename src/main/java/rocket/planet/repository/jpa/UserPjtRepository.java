package rocket.planet.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rocket.planet.domain.Profile;
import rocket.planet.domain.Project;
import rocket.planet.domain.UserProject;

public interface UserPjtRepository extends JpaRepository<UserProject, UUID> {
	List<UserProject> findAllByProject_Id(UUID requestedProject);

	List<UserProject> findAllByProject(Optional<Project> byProjectName);

	UserProject findByProject_projectNameAndProfile_userNickName(String projectName, String userNickName);

	List<UserProject> findAllByUserPjtCloseApply(boolean apply);

	List<UserProject> findAllByProfile_userNickName(String userNickName);

	List<UserProject> findAllByProfile(Profile teamMember);

	Optional<UserProject> findByProject_ProjectNameAndAndProfile_UserNickName(String projectName, String userNickName);

	List<UserProject> findByProfile(Profile user);

	List<Profile> findProfileByProject(Optional<Project> project);

	@Query(
		"select distinct up "
			+ "from UserProject up "
			+ "JOIN FETCH up.project p "
			+ "JOIN FETCH up.profile pf "
			+ "JOIN FETCH pf.org o "
			+ "JOIN FETCH o.department d "
			+ "where d.deptName = :deptName "
			+ "and pf.profileStatus = true "
			+ "and up.userPjtCloseApply = false "
			+ "and p.projectStatus = 'ONGOING' ")
	List<UserProject> findPjtPartCountByDepartment(@Param("deptName") String deptName);

	@Query(
		"select distinct up "
			+ "from UserProject up "
			+ "JOIN FETCH up.project p "
			+ "JOIN FETCH up.profile pf "
			+ "JOIN FETCH pf.org o "
			+ "JOIN FETCH o.team t "
			+ "where t.teamName = :teamName "
			+ "and pf.profileStatus = true "
			+ "and up.userPjtCloseApply = false "
			+ "and p.projectStatus = 'ONGOING' ")
	List<UserProject> findPjtPartCountByTeam(@Param("teamName") String teamName);

	@Query(
		"select distinct p "
			+ "from Profile p "
			+ "join FETCH p.org o "
			+ "JOIN FETCH o.department d "
			+ "where d.deptName = :deptName "
			+ "and p.profileStatus = true")
	List<Profile> findStatsByDepartment(@Param("deptName") String deptName);

	@Query(
		"select distinct up "
			+ "from UserProject up "
			+ "JOIN FETCH up.profile pf "
			+ "JOIN FETCH up.project p "
			+ "where pf.profileStatus = true "
			+ "and p.projectStatus = 'ONGOING' ")
	List<UserProject> findPjtPartCountByEntire();

	@Query(
		"select distinct p "
			+ "from Profile p "
			+ "join FETCH p.org o "
			+ "JOIN FETCH o.team t "
			+ "where t.teamName = :teamName "
			+ "and p.profileStatus = true")
	List<Profile> findStatsByTeam(@Param("teamName") String teamName);

	@Query(
		"select distinct up from UserProject up "
			+ "JOIN FETCH up.project p "
			+ "JOIN FETCH up.profile pf "
			+ "JOIN pf.org o "
			+ "JOIN o.team t "
			+ "where pf.profileStatus = true "
			+ "and p.projectStatus = 'ONGOING' "
			+ "and t.teamName = :teamName ")
	List<UserProject> findProjectStatsByTeam(@Param("teamName") String teamName);

	@Query(
		"select distinct up from UserProject up "
			+ "JOIN FETCH up.project p "
			+ "JOIN FETCH up.profile pf "
			+ "JOIN pf.org o "
			+ "JOIN o.department d "
			+ "where pf.profileStatus = true "
			+ "and p.projectStatus = 'ONGOING' "
			+ "and d.deptName = :deptName ")
	List<UserProject> findProjectStatsByDept(@Param("deptName") String deptName);

	@Query("select count(p) from Profile p where p.profileStatus = true group by p.profileStatus")
	int countProfileByEntire();

}
