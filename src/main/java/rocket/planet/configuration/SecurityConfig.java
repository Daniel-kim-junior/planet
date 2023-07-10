package rocket.planet.configuration;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import rocket.planet.util.security.CustomAccessDeniedHandler;
import rocket.planet.util.security.CustomAuthenticationEntryPoint;
import rocket.planet.util.security.JwtAuthenticationFilter;
import rocket.planet.util.security.JwtAuthenticationProvider;

/*
 * Spring Security 설정
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	private final JwtAuthenticationProvider jwtAuthenticationProvider;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf().disable();
		http.httpBasic().disable();

		http.formLogin().disable();

		http.cors().configurationSource(corsConfigurationSource());

		http.sessionManagement()
			.sessionCreationPolicy(STATELESS); // 세션을 사용하지 않음 (JWT 사용 시 필요 없음)

		http.authorizeRequests()
			.antMatchers("/**").permitAll()
			.antMatchers("/api/auth/**").permitAll()
			.antMatchers("/api/admin/**").hasRole("ADMIN")
			.anyRequest().authenticated();

		http.exceptionHandling()
			.authenticationEntryPoint(customAuthenticationEntryPoint) // 인증되지 않은 사용자가 접근하려 할 때
			.accessDeniedHandler(customAccessDeniedHandler); // 인가되지 않은 사용자가 접근하려 할 때
		http.apply(new JwtSecurityConfig(authenticationManagerBuilder
			.authenticationProvider(jwtAuthenticationProvider)
			.getOrBuild()));


		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(false); // 쿠키를 받을건지
		configuration.setAllowedOrigins(List.of("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PATCH", "OPTIONS"));
		configuration.addAllowedHeader("*");

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
