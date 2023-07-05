package rocket.planet.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Org;

public interface OrgRepository extends JpaRepository<Org, UUID> {
	Org findByUser_UserName(String userName);

	UUID findTeam_IdById(UUID userId);

	Optional<Org> findByUser_Id(UUID id);
}
