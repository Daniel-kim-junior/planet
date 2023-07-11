package rocket.planet.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Certification;
 
public interface CertRepository extends JpaRepository<Certification, UUID> {
}
