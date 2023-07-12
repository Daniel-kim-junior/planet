package rocket.planet.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Certification;
 
public interface CertRepository extends JpaRepository<Certification, UUID> {
    Optional<Certification> findByCertNumber(String certNumber);
    UUID findIdByCertNumber(String certNumber);
    Long deleteCertificationByCertNumber(String certNumber);

}
