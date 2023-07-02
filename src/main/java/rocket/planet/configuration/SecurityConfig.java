package rocket.planet.configuration;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import rocket.planet.util.security.CustomAccessDeniedHandler;
import rocket.planet.util.security.CustomAuthenticationEntryPoint;
import rocket.planet.util.security.SecurityAuthenticationFilter;

/*
 * Spring Security 설정
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return webSecurity -> webSecurity.ignoring()
			.antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**");
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf().disable();

		http.httpBasic().disable();

		http.formLogin().disable();

		http.cors().configurationSource(corsConfigurationSource());

		http.exceptionHandling()
			.authenticationEntryPoint(customAuthenticationEntryPoint) // 인증되지 않은 사용자가 접근하려 할 때
			.accessDeniedHandler(customAccessDeniedHandler); // 인가되지 않은 사용자가 접근하려 할 때

		http.sessionManagement()
			.sessionCreationPolicy(STATELESS); // 세션을 사용하지 않음 (JWT 사용 시 필요 없음)

		http
			.authorizeRequests()
			.antMatchers("/**").permitAll()
			.antMatchers("/login").permitAll()
			.antMatchers("/api/join/**").permitAll()
			.antMatchers("/api/email/**").permitAll()
			.anyRequest().authenticated();

		http
			.addFilterBefore(new SecurityAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(false); // 쿠키를 받을건지
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PATCH", "OPTIONS"));
		configuration.addAllowedHeader("*");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
