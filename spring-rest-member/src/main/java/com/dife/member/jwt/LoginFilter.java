package com.dife.member.jwt;

<<<<<<< HEAD
=======
import com.dife.member.exception.MemberNotFoundException;
import com.dife.member.exception.UnAuthorizationException;
import com.dife.member.model.Member;
>>>>>>> c3768c7 (에러 헨들링 코드 작성)
import com.dife.member.model.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil)
    {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try
        {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);
            return authenticationManager.authenticate(authToken);

<<<<<<< HEAD
        System.out.println(email);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        return authenticationManager.authenticate(authToken);
=======
        } catch (AuthenticationException e)
        {
            throw new AuthenticationServiceException("인증에 실패했습니다!", e);
        }
>>>>>>> c3768c7 (에러 헨들링 코드 작성)
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String email = customUserDetails.getUsername();


        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

<<<<<<< HEAD
        String token = jwtUtil.createJwt(email, role, 60*60*10L);
=======
//        Optional<Member> optionalMember = memberRepository.findByEmail(email);
//        Member member = optionalMember.get();

        String token = jwtUtil.createAccessJwt(email, role, 24 * 60 * 60 * 1000L);
//        member.setTokenId(token);
//
        String responseBody = "유저가 로그인했습니다.\n사용자 TokenID : " + token;
        ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(responseBody);


        response.setStatus(responseEntity.getStatusCode().value());
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

        response.getWriter().write(responseEntity.getBody());
>>>>>>> c3768c7 (에러 헨들링 코드 작성)

        response.addHeader("Authorization", "Bearer " + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(401);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("인증되지 않은 회원입니다!");
//        throw new UnAuthorizationException("인증되지 않은 회원입니다!");
    }
}
