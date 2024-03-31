package com.dife.member.jwt;

import com.dife.member.ExceptionResonse;
import com.dife.member.exception.ForbiddenException;
import com.dife.member.exception.MemberNotFoundException;
import com.dife.member.exception.UnAuthorizationException;
import com.dife.member.model.Member;
import com.dife.member.model.dto.CustomUserDetails;
import com.dife.member.repository.MemberRepository;
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
import java.security.SignatureException;
import java.util.Optional;


@Slf4j
public class JWTFilter  extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    public JWTFilter(JWTUtil jwtUtil, MemberRepository memberRepository)
    {
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterchain) throws ServletException, IOException
    {
        String token = jwtUtil.resolveToken(request);

        if (token == null) {
            log.info("토큰 없음");
            throw new UnAuthorizationException("회원만 접근 가능합니다!");
        }
        log.info("순수 토큰 획득");
        if (jwtUtil.isExpired(token))
        {
            log.info("만료된 토큰");
            throw new ForbiddenException("만료된 토큰입니다!");
        }

        log.info("토큰 획득 : ");
        String email = jwtUtil.getEmail(token);

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty())
        {
            throw new MemberNotFoundException("유저를 찾을 수 없습니다!");
        }
        Member member = optionalMember.get();

        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterchain.doFilter(request, response);

    }

}
