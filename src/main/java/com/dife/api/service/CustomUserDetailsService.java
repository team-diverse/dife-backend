package com.dife.api.service;

import com.dife.api.model.Member;
import com.dife.api.model.dto.CustomUserDetails;
import com.dife.api.repository.MemberRepository;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	public CustomUserDetailsService(MemberRepository memberRepository) {

		this.memberRepository = memberRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		if (memberRepository.existsByEmail(email)) {
			Optional<Member> optionalMember = memberRepository.findByEmail(email);
			Member member = optionalMember.get();
			return new CustomUserDetails(member);
		} else {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email);
		}
	}
}
