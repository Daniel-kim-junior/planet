package rocket.planet.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.PjtRecord;
 
public interface PjtRecordRepository extends JpaRepository<PjtRecord, UUID> {
    Optional<PjtRecord> findByProfile(UUID profileId);
}
