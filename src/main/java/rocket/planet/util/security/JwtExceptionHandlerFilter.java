package rocket.planet.util.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import rocket.planet.util.exception.JwtInvalidException;

@Component
@RequiredArgsConstructor
public class JwtExceptionHandlerFilter implements Filter {

	private final CustomAuthenticationEntryPoint authenticationEntryPoint;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {
		try {
			chain.doFilter(request, response);
		} catch (JwtInvalidException e) {
			authenticationEntryPoint.commence((HttpServletRequest)request, (HttpServletResponse)response, e);
		}
	}
}
