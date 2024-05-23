package com.dife.api.model.dto;

import com.dife.api.model.Member;
import java.util.ArrayList;
import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

	private final Member member;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> collection = new ArrayList<>();

		collection.add(
				new GrantedAuthority() {

					@Override
					public String getAuthority() {

						return member.getRole();
					}
				});

		return collection;
	}

	@Override
	public String getUsername() {

		return member.getEmail();
	}

	@Override
	public String getPassword() {

		return member.getPassword();
	}

	public Long getId() {
		return member.getId();
	}

	public Boolean getIsVerified() {
		return member.getIs_verified();
	}

	public String getVerificationFileId() {
		return member.getVerification_file_id();
	}

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	public boolean isEnabled() {

		return true;
	}
}
