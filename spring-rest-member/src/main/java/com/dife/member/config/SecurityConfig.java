package com.dife.member.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity

                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
//                .sessionManagement(
//                        sessionManagement ->
//                                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers("/api/members/register").permitAll();
                    requests.requestMatchers("/api/members/login").permitAll();
                    requests.requestMatchers("/api/members/**").permitAll();
                    requests.requestMatchers("/api/**").authenticated();
                })
//                .formLogin((formlogin) -> formlogin
//                        .loginPage("/api/members/login")
//                        .permitAll()
//                        .defaultSuccessUrl("/api/members/"))

                .build();
    }
}
