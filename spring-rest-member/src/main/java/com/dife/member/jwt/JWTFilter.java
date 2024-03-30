package com.dife.member.jwt;

import com.dife.member.model.Member;
import com.dife.member.model.dto.CustomUserDetails;
import com.dife.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;


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

        if (token == null)
        {
            filterchain.doFilter(request,response);
            return;
        }

        String email = jwtUtil.getEmail(token);
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = optionalMember.get();
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterchain.doFilter(request, response);

    }

}
