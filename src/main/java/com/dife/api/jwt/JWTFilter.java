package com.dife.api.jwt;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.dife.api.exception.MemberException;
import com.dife.api.model.Member;
import com.dife.api.model.dto.CustomUserDetails;
import com.dife.api.repository.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

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
		try {
			String token = jwtUtil.resolveToken(request);

			if (!jwtUtil.isExpired(token)) {
				Long id = jwtUtil.getId(token);
				Member member =
						memberRepository.findById(id).orElseThrow(() -> new MemberException("회원을 찾을 수 없습니다!"));

				if (member.getVerification_file_id() != null && !member.getIs_verified())
					throw new MemberException("인증받지 않은 회원입니다!");

				CustomUserDetails customUserDetails = new CustomUserDetails(member);
				Authentication authentication =
						new UsernamePasswordAuthenticationToken(
								customUserDetails, null, customUserDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
				filterChain.doFilter(request, response);
			}

			response.setStatus(UNAUTHORIZED.value());
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain; charset=UTF-8");
			response.getWriter().write("만료된 토큰입니다! 다시 로그인하세요!");

		} catch (MemberException | NullPointerException | ServletException e) {
			response.setStatus(UNAUTHORIZED.value());
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain; charset=UTF-8");
			response.getWriter().write("인증이 필요한 회원입니다!");
		} catch (IllegalArgumentException | ExpiredJwtException | MalformedJwtException e) {
			response.setStatus(UNAUTHORIZED.value());
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain; charset=UTF-8");
			response.getWriter().write("만료된 토큰입니다! 다시 로그인하세요!");
		} catch (IllegalStateException e) {
			response.setStatus(UNAUTHORIZED.value());
		}
	}

	private boolean isExemptPath(String servletPath) {
		return servletPath.startsWith("/api/members/register")
				|| servletPath.equals("/api/members/change-password")
				|| servletPath.equals("/api/members/login")
				|| servletPath.equals("/api/members/check-refreshToken");
	}
}
