package rocket.planet.util.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import rocket.planet.domain.Profile;
import rocket.planet.domain.Role;

@Getter
public class PlanetUser extends User {

	private Profile profile;

	private rocket.planet.domain.User user;

	public PlanetUser(rocket.planet.domain.User user) {
		super(user.getUserId(), user.getUserPwd(), authorities(Collections.singleton(Role.GUEST)));
		this.user = user;
	}

	public PlanetUser(rocket.planet.domain.User user, Profile profile) {
		super(user.getUserId(), user.getUserPwd(), authorities(Collections.singleton(profile.getRole())));
		this.profile = profile;
		this.user = user;
	}

	private static Collection<? extends GrantedAuthority> authorities(Set<Role> roles) {
		return roles.stream()
			.map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
			.collect(Collectors.toSet());
	}

}
