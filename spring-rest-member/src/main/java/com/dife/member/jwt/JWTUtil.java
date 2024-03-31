package com.dife.member.jwt;

<<<<<<< HEAD
=======
import com.dife.member.exception.UnAuthorizationException;
import com.dife.member.repository.MemberRepository;
import com.dife.member.service.CustomUserDetailsService;
>>>>>>> c3768c7 (에러 헨들링 코드 작성)
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${jwt.secret}")String secret) {


        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
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

    public String createJwt(String email, String role, Long expiredMs) {

        return Jwts.builder()
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
<<<<<<< HEAD
=======

    public String resolveToken(HttpServletRequest request) {
        String authorization= request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer "))
        {
            throw new UnAuthorizationException("인증되지 않은 회원입니다!");
        }
        return authorization.split(" ")[1];
    }


>>>>>>> c3768c7 (에러 헨들링 코드 작성)
}
