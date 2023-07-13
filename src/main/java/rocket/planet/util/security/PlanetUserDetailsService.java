package rocket.planet.util.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import rocket.planet.domain.User;
import rocket.planet.repository.jpa.UserRepository;

@RequiredArgsConstructor
@Component
public class PlanetUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUserId(username)
			.orElseThrow(() -> new UsernameNotFoundException(username));

		return new PlanetUser(user, user.getProfile());
	}
}
