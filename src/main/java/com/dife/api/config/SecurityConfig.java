package com.dife.api.config;

import com.dife.api.jwt.JWTFilter;
import com.dife.api.jwt.JWTUtil;
import com.dife.api.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

	private JWTUtil jwtUtil;
	private MemberRepository memberRepository;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
			throws Exception {

		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

		return httpSecurity
				.httpBasic(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)
				.addFilterBefore(
						new JWTFilter(jwtUtil, memberRepository), UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(
						sessionManagement ->
								sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(
						requests -> {
							requests
									.requestMatchers(
											"/swagger-ui/**",
											"/api/v1/api-docs/**",
											"/api/members/register",
											"/api/members/refresh-token",
											"/api/members/change-password",
											"/api/members/login",
											"/health",
											"/ws/**")
									.permitAll();
							requests.requestMatchers("/api/**").authenticated();
						})
				.build();
	}
}
