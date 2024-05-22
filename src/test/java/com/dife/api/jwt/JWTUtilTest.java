package com.dife.api.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JWTUtilTest {
	private static JWTUtil jwtUtil;
	private static SecretKey secretKey;

	@Mock private HttpServletRequest request;

	@BeforeAll
	public static void setUp() {
		String secret = "cef2660faf36986dca4d1c4b5850eaf0be4900af9713f11a9ef86c952eb53d0c";
		secretKey =
				new SecretKeySpec(
						secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
		jwtUtil = new JWTUtil(secret);
	}

	@Test
	public void createAccessJwt_ShouldContainClaims_WhenTokenIsCreated() {
		String email = "user@example.com";
		String role = "user";
		Boolean is_verified = true;
		String verification_file_id = "학생증.jpg";
		Long duration = 1000L * 60 * 60;

		String token =
				jwtUtil.createAccessJwt(email, role, is_verified, verification_file_id, duration);
		assertNotNull(token);

		Claims claims =
				Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

		assertEquals(email, claims.get("email"));
		assertEquals(role, claims.get("role"));
		assertEquals(is_verified, claims.get("is_verified"));
		assertEquals(verification_file_id, claims.get("verification_file_id"));
		assertNotNull(claims.getIssuedAt());
		assertNotNull(claims.getExpiration());
	}

	@Test
	public void getEmail_ShouldReturnEmail_WhenTokenPassed() {
		String email = "user@example.com";
		String token = jwtUtil.createAccessJwt(email, "user", true, "학생증.jpg", 1000L * 60 * 60);

		assertEquals(email, jwtUtil.getEmail(token));
	}

	@Test
	public void getRole_ShouldReturnRole_WhenTokenPassed() {
		String role = "admin";
		String token =
				jwtUtil.createAccessJwt("user@example.com", role, true, "학생증.jpg", 1000L * 60 * 60);

		assertEquals(role, jwtUtil.getRole(token));
	}

	@Test
	public void isExpired_ShouldReturnExpiredJwtException_WhenTokenExpired() {
		String token = jwtUtil.createAccessJwt("user@example.com", "user", true, "학생증.jpg", -1000L);

		assertThrows(ExpiredJwtException.class, () -> jwtUtil.isExpired(token));
	}

	@Nested
	class ResolveTokenMethod {
		@Test
		public void shouldReturnToken_WhenValidToken() {
			when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
			assertEquals("validToken", jwtUtil.resolveToken(request));
		}

		@Test
		public void shouldReturnNull_WhenInvalidToken() {
			when(request.getHeader("Authorization")).thenReturn("Invalid");
			assertNull(jwtUtil.resolveToken(request));
		}

		@Test
		public void shouldReturnNull_WhenNull() {
			when(request.getHeader("Authorization")).thenReturn(null);
			assertNull(jwtUtil.resolveToken(request));
		}
	}
}
