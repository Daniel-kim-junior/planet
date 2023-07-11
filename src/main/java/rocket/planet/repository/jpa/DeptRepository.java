package rocket.planet.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rocket.planet.domain.Department;

public interface DeptRepository extends JpaRepository<Department, UUID> {

	Department findByDeptName(String deptName);

	@Query("SELECT d.deptName FROM Department d")
	List<String> findDeptNameAll();

}

