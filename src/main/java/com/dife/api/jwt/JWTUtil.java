package com.dife.api.jwt;

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

	public String createAccessJwt(
			String email, String role, Boolean is_verified, String verification_file_id, Long expiredMs) {

		return Jwts.builder()
				.claim("email", email)
				.claim("role", role)
				.claim("is_verified", is_verified)
				.claim("verification_file_id", verification_file_id)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expiredMs))
				.signWith(secretKey)
				.compact();
	}

	public String createRefreshJwt(
			String email, String role, Boolean is_verified, String verification_file_id, Long expiredMs) {

		return Jwts.builder()
				.claim("email", email)
				.claim("role", role)
				.claim("is_verified", is_verified)
				.claim("verification_file_id", verification_file_id)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expiredMs))
				.signWith(secretKey)
				.compact();
	}

	public String getEmail(String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get("email", String.class);
	}

	public String getRole(String token) {

		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get("role", String.class);
	}

	public Boolean getIsVerified(String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get("is_verified", Boolean.class);
	}

	public String getVerificationFileId(String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get("verification_file_id", String.class);
	}

	public Boolean isExpired(String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getExpiration()
				.before(new Date());
	}

	public String resolveToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.startsWith("Bearer ")) {
			return authorization.split(" ")[1];
		}
		return null;
	}
}
