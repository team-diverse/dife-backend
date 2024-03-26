package com.dife.member.security;

import com.dife.member.model.Member;
import com.dife.member.model.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
public class JWTFilter  extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization= request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            // ERROR 던져야함!
            log.error("토큰 없음");
            filterChain.doFilter(request, response);

            return;
        }

        log.info("인증됨");

        String token = authorization.split(" ")[1];


        if (jwtUtil.isExpired(token))
        {

            log.info("토큰 만료됨");
            filterChain.doFilter(request, response);

            return;
        }

        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);


        Member member = new Member();
        member.setEmail(email);
        member.setPassword("password");
        member.setIs_public(true);
        member.setIs_korean(true);
        member.setRole(role);
        member.setUsername("username");
        member.setMajor("major");

        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }


}
