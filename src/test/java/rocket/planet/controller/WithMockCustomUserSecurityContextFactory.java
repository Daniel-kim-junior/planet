package rocket.planet.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/*
 * 테스트용 커스텀 유저 어노테이션 팩토리
 */
public class WithMockCustomUserSecurityContextFactory implements
	WithSecurityContextFactory<WithMockCustomUser> {

	@Override
	public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		// 필요한 권한 설정
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("username",
			"password", authorities);

		context.setAuthentication(authentication);
		return context;
	}

}
