package com.dife.member.model.dto;

import com.dife.member.model.Member;
import lombok.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final Member member;

    public CustomUserDetails(Member member)
    {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

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
