package rocket.planet.service.stats;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.domain.Org;
import rocket.planet.repository.jpa.OrgRepository;

@SpringBootTest
public class Test {

	@Autowired
	private OrgRepository orgRepository;

	@org.junit.jupiter.api.Test
	@Transactional
	public void test() {
		List<Org> 경영지원 = orgRepository.findProjectStatsByDept("경영지원");
		경영지원.stream().forEach(org -> System.out.println(org.getTeam().getTeamName()));
	}
}
