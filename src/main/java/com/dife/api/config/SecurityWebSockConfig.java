package com.dife.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityWebSockConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				authorize ->
						authorize
								.requestMatchers("/pub/**")
								.authenticated()
								.requestMatchers("/sub/**")
								.authenticated()
								.anyRequest()
								.permitAll());
		return http.build();
	}
}
