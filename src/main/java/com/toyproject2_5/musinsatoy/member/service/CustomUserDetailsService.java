package com.toyproject2_5.musinsatoy.member.service;

import com.toyproject2_5.musinsatoy.member.join.repository.MemberDao;
import com.toyproject2_5.musinsatoy.member.join.dto.MemberDto;
import com.toyproject2_5.musinsatoy.member.mapper.MemberDtoMapper;
import com.toyproject2_5.musinsatoy.member.model.CustomUserDetails;
import com.toyproject2_5.musinsatoy.member.model.Member;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberDao memberDao;

    public CustomUserDetailsService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // 비밀번호 암호화
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberDto memberDto = this.memberDao.selectOne(username);
        if(memberDto == null)
            throw new UsernameNotFoundException("아이디 "+username+" 을 찾을 수 없습니다.");
        Member member = MemberDtoMapper.INSTANCE.toEntity(memberDto);

        return new CustomUserDetails(member, passwordEncoder());
    }
}
