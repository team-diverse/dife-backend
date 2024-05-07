package com.dife.api.jwt;

import com.dife.api.model.Member;
import com.dife.api.model.dto.CustomUserDetails;
import com.dife.api.repository.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
	private static final String AUTH_HEADER = "Authorization";
	private static final String BEARER_TOKEN_PREFIX = "Bearer ";
	private static final long TOKEN_VALIDITY_DURATION = 14 * 24 * 60 * 60 * 1000L;

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

	private void handleExpiredToken(ExpiredJwtException e, HttpServletResponse response) {
		String email = e.getClaims().get("email", String.class);
		String role = e.getClaims().get("role", String.class);

		String newToken = jwtUtil.createAccessJwt(email, role, TOKEN_VALIDITY_DURATION);

		Optional<Member> optionalMember = memberRepository.findByEmail(email);

		Member member = optionalMember.get();
		member.setTokenId(newToken);
		memberRepository.save(member);

		response.setHeader(AUTH_HEADER, BEARER_TOKEN_PREFIX + newToken);
	}

	private boolean isExemptPath(String servletPath) {
		return servletPath.startsWith("/api/members/register")
				|| servletPath.equals("/api/members/change-password")
				|| servletPath.equals("/api/members/login")
				|| servletPath.startsWith("/swagger-ui")
				|| servletPath.startsWith("/api/chat")
				|| servletPath.startsWith("/ws")
				|| servletPath.equals("/api/v1/api-docs");
	}
}
