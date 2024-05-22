package com.dife.api.jwt;

import static org.springframework.http.HttpStatus.CREATED;

import com.dife.api.exception.MemberNullException;
import com.dife.api.model.Member;
import com.dife.api.model.dto.CustomUserDetails;
import com.dife.api.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
	private static final String AUTH_HEADER = "Authorization";
	private static final String BEARER_TOKEN_PREFIX = "Bearer ";
	private static final long TOKEN_VALIDITY_DURATION = 90 * 24 * 60 * 60 * 1000L;

	private final JWTUtil jwtUtil;
	private final MemberRepository memberRepository;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String servletPath = request.getServletPath();

		if (isExemptPath(servletPath)) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = jwtUtil.resolveToken(request);

		try {
			String email = jwtUtil.getEmail(token);
			memberRepository.findByEmail(email).ifPresent(this::authenticateUser);
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			handleExpiredToken(e, response);
			filterChain.doFilter(request, response);
		} catch (JwtException | IllegalArgumentException | NoSuchElementException e) {
			filterChain.doFilter(request, response);
		}
	}

	private void authenticateUser(Member member) {
		CustomUserDetails customUserDetails = new CustomUserDetails(member);
		Authentication authentication =
				new UsernamePasswordAuthenticationToken(
						customUserDetails, null, customUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private void handleExpiredToken(ExpiredJwtException e, HttpServletResponse response)
			throws IOException, MemberNullException {
		String email = e.getClaims().get("email", String.class);
		String role = e.getClaims().get("role", String.class);
		Boolean is_verified = e.getClaims().get("is_verified", Boolean.class);
		String verification_file_id = e.getClaims().get("verification_file_id", String.class);

		if (is_verified && verification_file_id != null) {
			String refreshToken =
					jwtUtil.createRefreshJwt(
							email, role, is_verified, verification_file_id, TOKEN_VALIDITY_DURATION);

			ResponseEntity<String> responseEntity =
					ResponseEntity.status(CREATED).body("{ \"refreshToken\": \"" + refreshToken + "\" }");

			String responseBody = new ObjectMapper().writeValueAsString(responseEntity.getBody());
			response.getWriter().write(responseBody);
		}
	}

	private boolean isExemptPath(String servletPath) {
		return servletPath.startsWith("/api/members/register")
				|| servletPath.equals("/api/members/change-password")
				|| servletPath.equals("/api/members/login")
				|| servletPath.startsWith("/swagger-ui")
				|| servletPath.equals("/api/v1/api-docs");
	}
}
