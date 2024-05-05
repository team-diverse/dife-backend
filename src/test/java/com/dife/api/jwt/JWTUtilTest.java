package com.dife.api.jwt;

import static org.junit.jupiter.api.Assertions.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

public class JWTUtilTest {
	private static JWTUtil jwtUtil;
	private static SecretKey secretKey;


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
		Long duration = 1000L * 60 * 60;

		String token = jwtUtil.createAccessJwt(email, role, duration);
		assertNotNull(token);

		Claims claims =
				Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

		assertEquals(email, claims.get("email"));
		assertEquals(role, claims.get("role"));
		assertNotNull(claims.getIssuedAt());
		assertNotNull(claims.getExpiration());
	}

}
