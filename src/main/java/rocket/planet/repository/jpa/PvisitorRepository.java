package rocket.planet.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import rocket.planet.domain.ProfileVisitor;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PvisitorRepository extends JpaRepository<ProfileVisitor, UUID> {
    List<ProfileVisitor> findByOwner_UserNickName(String userNickName);
    List<ProfileVisitor> findByVisitTimeBefore(LocalDate date);
    Optional<ProfileVisitor> findByVisitor_UserNickNameAndOwner_UserNickName(String visitor, String owner);

}
