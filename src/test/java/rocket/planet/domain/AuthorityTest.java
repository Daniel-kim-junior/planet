package rocket.planet.domain;

import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import rocket.planet.CustomJpaTest;
import rocket.planet.repository.jpa.AuthRepository;

@CustomJpaTest
@Transactional
public class AuthorityTest {

	@Autowired
	private AuthRepository em;

	@Test
	void test() {
		Authority authority = Authority.builder()
			.authorizerId("ss@gmail.com")
			.authType(AuthType.COMPANY)
			.authTargetId(UUID.randomUUID())
			.build();
		Authority save = em.save(authority);
		List<Authority> all = em.findAll();
		Assertions.assertThat(all.get(0).getId()).isEqualTo(save.getId());
	}

}
