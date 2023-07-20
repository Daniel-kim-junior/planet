package rocket.planet.util.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import rocket.planet.repository.jpa.UserRepository;
import rocket.planet.repository.redis.AccessTokenRedisRepository;
import rocket.planet.util.exception.JwtInvalidException;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final String KEY_ROLES = "roles";

	private final byte[] secretKey;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PlanetUserDetailsService planetUserDetailsService;

	@Autowired
	private AccessTokenRedisRepository accessTokenRedisRepository;

	public JwtAuthenticationProvider(@Value("${jwt.secret}") String secretKey) {
		this.secretKey = secretKey.getBytes();
	}

	private Collection<? extends GrantedAuthority> createGrantedAuthorities(Claims claims) {
		List<String> roles = (List)claims.get(KEY_ROLES);
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		for (String role : roles) {
			grantedAuthorities.add(() -> role);
		}
		return grantedAuthorities;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Claims claims;
		try {
			claims = Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(((JwtAuthenticationToken)authentication).getToken())
				.getBody();
		} catch (SignatureException signatureException) {
			throw new JwtInvalidException("Invalid JWT signature", signatureException);
		} catch (ExpiredJwtException expiredJwtException) {
			throw new JwtInvalidException("Expired JWT token", expiredJwtException);
		} catch (MalformedJwtException malformedJwtException) {
			throw new JwtInvalidException("Malformed JWT token", malformedJwtException);
		} catch (IllegalArgumentException illegalArgumentException) {
			throw new JwtInvalidException("using illegal argument like null", illegalArgumentException);
		}
		PlanetUser planetUser = (PlanetUser)planetUserDetailsService.loadUserByUsername(claims.getSubject());

		accessTokenRedisRepository.findById(planetUser.getUsername())
			.orElseThrow(() -> new JwtInvalidException("Invalid JWT token"));
		return new JwtAuthenticationToken(planetUser, "", createGrantedAuthorities(claims));
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
