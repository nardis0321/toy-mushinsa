package com.toyproject2_5.musinsatoy.member.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class CustomUserDetails extends Member implements UserDetails {

    private static final List<GrantedAuthority> ROLE_USER = Collections
            .unmodifiableList(AuthorityUtils.createAuthorityList("ROLE_USER"));

    public CustomUserDetails(Member member, PasswordEncoder passwordEncoder){
        super(member.getMember_number(), member.getId(), passwordEncoder.encode(member.getPassword()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return ROLE_USER;     // 권한 반환
    }

    @Override
    public String getUsername() {   // 고유식별자 반환 - pk는 memberNumber이지만 id도 unique한 상태
        return getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !(Objects.equals(getMember_state_code(), "40002")); // 휴면
    }

    @Override
    public boolean isAccountNonLocked() {

        return !(Objects.equals(getMember_state_code(), "40003")); // 잠금
    }

    @Override
    public boolean isCredentialsNonExpired() {  // 자격 증명의 만료 여부
//        return UserDetails.super.isCredentialsNonExpired();
        return true; // 공식문서 설정... 옳은가?
    }

    @Override
    public boolean isEnabled() {
        return Objects.equals(getMember_state_code(), "40001");
    }
}
