package com.dife.api.controller;

import com.dife.api.model.Member;
import com.dife.api.model.dto.CustomUserDetails;
import com.dife.api.repository.MemberRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory
		implements WithSecurityContextFactory<WithMockCustomUser> {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public WithMockCustomUserSecurityContextFactory(
			MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public SecurityContext createSecurityContext(WithMockCustomUser annotation) {

		Member member = new Member();
		member.setId(annotation.id());
		member.setEmail(annotation.email());
		member.setPassword(passwordEncoder.encode(annotation.password()));
		memberRepository.save(member);

		CustomUserDetails customUserDetails = new CustomUserDetails(member);
		Authentication authentication =
				new UsernamePasswordAuthenticationToken(
						customUserDetails, null, customUserDetails.getAuthorities());

		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(authentication);
		return securityContext;
	}
}
