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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;
	private final MemberRepository memberRepository;

	public JWTFilter(JWTUtil jwtUtil, MemberRepository memberRepository) {

		this.jwtUtil = jwtUtil;
		this.memberRepository = memberRepository;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String servletPath = request.getServletPath();
		String token = jwtUtil.resolveToken(request);

		if (servletPath.startsWith("/api/members/register")
				|| servletPath.equals("/api/members/change-password")
				|| servletPath.equals("/api/members/login")
				|| servletPath.startsWith("/swagger-ui")
				|| servletPath.startsWith("/api/chat")
				|| servletPath.startsWith("/ws")
				|| servletPath.equals("/api/v1/api-docs")) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			log.info("AceessToken : " + token);

			String email = jwtUtil.getEmail(token);
			Optional<Member> optionalMember = memberRepository.findByEmail(email);
			Member member = optionalMember.get();

			CustomUserDetails customUserDetails = new CustomUserDetails(member);
			Authentication authentication =
					new UsernamePasswordAuthenticationToken(
							customUserDetails, null, customUserDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);

			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			log.info("만료된 토큰");

			String email = e.getClaims().get("email", String.class);
			String role = e.getClaims().get("role", String.class);

			String newToken = jwtUtil.createAccessJwt(email, role, 14 * 24 * 60 * 60 * 1000L);

			Optional<Member> optionalMember = memberRepository.findByEmail(email);

			Member member = optionalMember.get();
			member.setTokenId(newToken);
			memberRepository.save(member);

			log.info("Refresh Token : " + member.getTokenId());

			response.setHeader("Authorization", "Bearer " + newToken);
			filterChain.doFilter(request, response);

		} catch (JwtException | IllegalArgumentException e) {
			log.error("유효하지 않은 토큰이 입력되었습니다.");
			filterChain.doFilter(request, response);
		} catch (NullPointerException e) {
			log.info("사용자를 찾을 수 없습니다.");
			filterChain.doFilter(request, response);
		} catch (NoSuchElementException e) {
			log.error("NoSuchElementException");
			filterChain.doFilter(request, response);
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error("토큰을 추출할 수 없습니다.");
			filterChain.doFilter(request, response);
		}
	}
}
