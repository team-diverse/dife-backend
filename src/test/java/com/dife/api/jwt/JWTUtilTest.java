package com.dife.api.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Claims;
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
		Long id = 1L;
		String type = "AccessToken";
		String issuer = "dife";
		Long duration = 1000L * 60 * 60;

		String token = jwtUtil.createJwt(1L, type, issuer, duration);
		assertNotNull(token);

		Claims claims =
				Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

		assertEquals(id, Long.valueOf(claims.get("id", Integer.class)));
		assertEquals(type, claims.get("type"));
		assertNotNull(claims.getIssuedAt());
		assertNotNull(claims.getExpiration());
	}

	@Test
	public void getId_ShouldReturnId_WhenTokenPassed() {
		Long id = 1L;
		String token = jwtUtil.createJwt(id, "accessToken", "dife", 1000L * 60 * 60);

		assertEquals(id, jwtUtil.getId(token));
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
