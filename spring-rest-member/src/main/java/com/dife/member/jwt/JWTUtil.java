package com.dife.member.jwt;

import com.dife.member.repository.MemberRepository;
import com.dife.member.service.CustomUserDetailsService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;
    private CustomUserDetailsService userDetailsService;
    private MemberRepository memberRepository;

    public JWTUtil(@Value("${jwt.secret}")String secret, CustomUserDetailsService userDetailsService, MemberRepository memberRepository) {

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.userDetailsService = userDetailsService;
        this.memberRepository = memberRepository;
    }

    public String getEmail(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createAccessJwt(String email, String role, Long expiredMs) {

        String token =  Jwts.builder()
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();

//        Optional<Member> optionalMember = memberRepository.findByEmail(email);
//        Member member = optionalMember.get();
//        member.setTokenId(token);

        return token;
    }

    public String resolveToken(HttpServletRequest request)
    {
        String authorization= request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer "))
        {
            return authorization.split(" ")[1];
        }

        return null;
    }


}
