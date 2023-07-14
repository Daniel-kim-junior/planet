package rocket.planet.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Profile;
import rocket.planet.domain.Project;
import rocket.planet.domain.UserProject;

public interface UserPjtRepository extends JpaRepository<UserProject, UUID> {
	List<UserProject> findAllByProject_Id(UUID requestedProject);

	List<UserProject> findAllByProject(Optional<Project> byProjectName);

	UserProject findByProject_projectNameAndProfile_userNickName(String projectName, String userNickName);

	List<UserProject> findAllByUserPjtCloseApply(boolean apply);

	List<UserProject> findAllByProfile_userNickName(String userNickName);

	UserProject findByProject(Optional<Project> project);

	List<UserProject> findAllByProfile(Profile teamMember);

	Optional<UserProject> findByProject_ProjectNameAndAndProfile_UserNickName(String projectName, String userNickName);

	List<Profile> findProfileByProject(Optional<Project> project);
}
