package rocket.planet.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rocket.planet.domain.Department;

public interface DeptRepository extends JpaRepository<Department, UUID> {
	Department findByDeptName(String deptName);
}
