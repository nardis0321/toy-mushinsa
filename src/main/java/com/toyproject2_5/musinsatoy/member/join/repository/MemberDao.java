package com.toyproject2_5.musinsatoy.member.join.repository;

import com.toyproject2_5.musinsatoy.member.join.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

import java.lang.reflect.Member;

@Mapper
public interface MemberDao {
    void deleteAll();                       // 전체 회원 삭제
    int count();                            // 전체 회원수 카운트
    int insertMember(MemberDto memberDto);  // 회원 가입
    MemberDto selectOne(String id);         // 회원 1명 정보 가져오기
}
