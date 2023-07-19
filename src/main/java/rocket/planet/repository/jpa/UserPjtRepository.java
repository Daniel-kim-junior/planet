package rocket.planet.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
			+ "and up.userPjtCloseApply = false "
			+ "and p.projectStatus = 'ONGOING' ")
	List<UserProject> findPjtPartCountByDepartment(String deptName);

	@Query(
		"select distinct up "
			+ "from UserProject up "
			+ "JOIN FETCH up.project p "
			+ "JOIN FETCH up.profile pf "
			+ "JOIN FETCH pf.org o "
			+ "JOIN FETCH o.department d "
			+ "where d.deptName = :deptName "
			+ "and (up.userPjtCloseApply = true "
			+ "or p.projectStatus != 'ONGOING') ")
	List<UserProject> findPjtPartCountByDepartmentClosed(String deptName);

	@Query(
		"select distinct up "
			+ "from UserProject up "
			+ "JOIN FETCH up.project p "
			+ "JOIN FETCH up.profile pf "
			+ "JOIN FETCH pf.org o "
			+ "JOIN FETCH o.team t "
			+ "where t.teamName = :teamName "
			+ "and up.userPjtCloseApply = false "
			+ "and p.projectStatus = 'ONGOING' ")
	List<UserProject> findPjtPartCountByTeam(String teamName);

	@Query(
		"select distinct up "
			+ "from UserProject up "
			+ "JOIN FETCH up.project p "
			+ "JOIN FETCH up.profile pf "
			+ "JOIN FETCH pf.org o "
			+ "JOIN FETCH o.team t "
			+ "where t.teamName = :teamName "
			+ "and (up.userPjtCloseApply = true "
			+ "or p.projectStatus != 'ONGOING') ")
	List<UserProject> findPjtPartCountByTeamClosed(String teamName);

	@Query(
		"select distinct up "
			+ "from UserProject up "
			+ "JOIN FETCH up.project p "
			+ "where up.userPjtCloseApply = false "
			+ "and p.projectStatus = 'ONGOING' ")
	List<UserProject> findPjtPartCountByEntire();

	@Query(
		"select distinct up "
			+ "from UserProject up "
			+ "JOIN FETCH up.project p "
			+ "where up.userPjtCloseApply = true "
			+ "or p.projectStatus != 'ONGOING' ")
	List<UserProject> findPjtPartCountByEntireClosed();

	@Query(
		"select distinct up "
			+ "from UserProject up "
			+ "JOIN FETCH up.project p "
			+ "JOIN FETCH up.profile pf "
			+ "JOIN FETCH pf.org o "
			+ "JOIN FETCH o.team t "
			+ "where t.teamName = :teamName "
			+ "and up.userPjtCloseApply = false "
			+ "and p.projectStatus = 'ONGOING' ")
	List<UserProject> findProjectStatsByTeam(String teamName);

	@Query(
		"select distinct up "
			+ "from UserProject up "
			+ "JOIN FETCH up.project p "
			+ "JOIN FETCH up.profile pf "
			+ "JOIN FETCH pf.org o "
			+ "JOIN FETCH o.department d "
			+ "where d.deptName = :deptName "
			+ "and up.userPjtCloseApply = false "
			+ "and p.projectStatus = 'ONGOING' ")
	List<UserProject> findProjectStatsByDept(String deptName);
}
