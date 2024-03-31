package com.dife.member.jwt;

import com.dife.member.ExceptionResonse;
import com.dife.member.exception.ForbiddenException;
import com.dife.member.exception.MemberNotFoundException;
import com.dife.member.exception.UnAuthorizationException;
import com.dife.member.model.Member;
import com.dife.member.model.dto.CustomUserDetails;
import com.dife.member.model.dto.LoginDto;
import com.dife.member.repository.MemberRepository;
import com.dife.member.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
<<<<<<< HEAD
=======
import java.security.SignatureException;
import java.util.Optional;
>>>>>>> c3768c7 (에러 헨들링 코드 작성)


@Slf4j
public class JWTFilter  extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

<<<<<<< HEAD
        String authorization= request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer "))
        {

            System.out.println("토큰 없음");
            filterChain.doFilter(request, response);

            return;
        }

        System.out.println("인증됨");

        String token = authorization.split(" ")[1];


        if (jwtUtil.isExpired(token))
        {

            System.out.println("토큰 만료됨");
            filterChain.doFilter(request, response);

            return;
=======
        if (token == null) {
            log.info("토큰 없음");
            throw new UnAuthorizationException("회원만 접근 가능합니다!");
        }
        log.info("순수 토큰 획득");
        if (jwtUtil.isExpired(token))
        {
            log.info("만료된 토큰");
            throw new ForbiddenException("만료된 토큰입니다!");
>>>>>>> c3768c7 (에러 헨들링 코드 작성)
        }

        log.info("토큰 획득 : ");
        String email = jwtUtil.getEmail(token);
<<<<<<< HEAD
        String role = jwtUtil.getRole(token);


        Member member = new Member();
        member.setEmail(email);
        member.setPassword("password");
        member.setIs_public(true);
        member.setIs_korean(true);
        member.setRole(role);
        member.setUsername("username");
        member.setMajor("major");
=======

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty())
        {
            throw new MemberNotFoundException("유저를 찾을 수 없습니다!");
        }
        Member member = optionalMember.get();
>>>>>>> c3768c7 (에러 헨들링 코드 작성)

        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }


}
