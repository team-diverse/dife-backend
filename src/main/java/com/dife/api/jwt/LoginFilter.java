package com.dife.api.jwt;

import static org.springframework.http.HttpStatus.CREATED;

import com.dife.api.ExceptionResonse;
import com.dife.api.model.dto.CustomUserDetails;
import com.dife.api.model.dto.LoginSuccessDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;

	public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.setFilterProcessesUrl("/api/members/login");
	}

	@Override
	public Authentication attemptAuthentication(
			HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		log.info("email : " + email);
		log.info("password : " + password);
		if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
			String errorMessage = "이메일과 비밀번호는 필수 사항";
			log.warn("이메일과 비밀번호는 필수 사항");

			ExceptionResonse exceptionResponse = new ExceptionResonse(false, errorMessage);

			ObjectMapper objectMapper = new ObjectMapper();
			String result = null;
			try {
				result = objectMapper.writeValueAsString(exceptionResponse);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		UsernamePasswordAuthenticationToken authToken =
				new UsernamePasswordAuthenticationToken(email, password, null);
		return authenticationManager.authenticate(authToken);
	}

	@Override
	protected void successfulAuthentication(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain chain,
			Authentication authentication)
			throws IOException {

		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

		String email = customUserDetails.getUsername();
		Long id = customUserDetails.getId();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String role = auth.getAuthority();

		String token = jwtUtil.createAccessJwt(email, role, 60 * 60 * 1000L);
		ResponseEntity<LoginSuccessDto> responseEntity =
				ResponseEntity.status(CREATED).body(new LoginSuccessDto(token, id));

		String responseBody = new ObjectMapper().writeValueAsString(responseEntity.getBody());
		response.getWriter().write(responseBody);
		response.addHeader("Authorization", "Bearer " + token);
	}

	@Override
	protected void unsuccessfulAuthentication(
			HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
			throws IOException {

		log.error("로그인 실패");
		String errorMessage = "인증에 실패했습니다: " + failed.getMessage();

		ExceptionResonse exceptionResponse = new ExceptionResonse(false, errorMessage);

		ObjectMapper objectMapper = new ObjectMapper();
		String result = objectMapper.writeValueAsString(exceptionResponse);

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		response.getWriter().write(result);
	}
}
