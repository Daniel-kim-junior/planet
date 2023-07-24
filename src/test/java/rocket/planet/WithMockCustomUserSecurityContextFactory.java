package rocket.planet;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import rocket.planet.util.security.JwtAuthenticationToken;

public class WithMockCustomUserSecurityContextFactory implements
	WithSecurityContextFactory<WithPlanetTestUser> {

	@Override
	public SecurityContext createSecurityContext(WithPlanetTestUser annotation) {
		String email = annotation.email();
		String role = annotation.role();
		PlanetTestUser testUser = new PlanetTestUser("testUser", email);

		JwtAuthenticationToken authentication = new JwtAuthenticationToken(
			testUser, "password", List.of(new SimpleGrantedAuthority(role)));
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentication);

		return securityContext;
	}

}
