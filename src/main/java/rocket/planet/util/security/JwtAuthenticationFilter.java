package rocket.planet.util.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rocket.planet.util.exception.JwtInvalidException;

/*
 * JWT 인증 필터(Spring Security Filter)
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String BEARER_PREFIX = "Bearer ";
	private final AuthenticationManager authenticationManager;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String jwt = resolveToken(request);

		if (StringUtils.hasText(jwt) && checkJwtHeaderUrl(request)) {
			try {

				Authentication jwtAuthenticationToken = new JwtAuthenticationToken(jwt);

				Authentication authentication = authenticationManager.authenticate(jwtAuthenticationToken);

				SecurityContextHolder.getContext().setAuthentication(authentication);

			} catch (JwtInvalidException e) {
				log.debug("Invalid JWT Token", e);
				SecurityContextHolder.clearContext();
				throw e;
			}

		}
		filterChain.doFilter(request, response);
	}

	private boolean checkJwtHeaderUrl(HttpServletRequest request) {
		return !request.getRequestURI().equals("/api/auth/reissue") && !request.getRequestURI()
			.equals("/api/auth/join-dept") && !request.getRequestURI().equals("/api/auth/join-team");
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
