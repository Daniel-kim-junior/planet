package rocket.planet.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Company;
 
public interface CompanyRepository extends JpaRepository<Company, UUID> {
	Company findByCompanyName(String companyName);
}
