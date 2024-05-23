package com.dife.api.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

	private SecretKey secretKey;

	public JWTUtil(@Value("${jwt.secret}") String secret) {

		secretKey =
				new SecretKeySpec(
						secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
	}

	public String createJwt(Long id, String role, String type, String issuer, Long expiredMs) {

		return Jwts.builder()
				.claim("id", id)
				.claim("role", role)
				.claim("type", type)
				.claim("iss", issuer)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expiredMs))
				.signWith(secretKey)
				.compact();
	}

	public Long getId(String token) {

		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get("id", Long.class);
	}

	public String getRole(String token) {

		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get("role", String.class);
	}

	public Boolean isExpired(String token) {

		try {
			return Jwts.parser()
					.verifyWith(secretKey)
					.build()
					.parseSignedClaims(token)
					.getPayload()
					.getExpiration()
					.before(new Date());
		} catch (ExpiredJwtException e) {
			return true;
		}
	}

	public String resolveToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.startsWith("Bearer ")) {
			return authorization.split(" ")[1];
		}
		return null;
	}
}
