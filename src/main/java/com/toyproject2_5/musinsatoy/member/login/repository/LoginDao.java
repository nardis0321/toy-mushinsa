package com.toyproject2_5.musinsatoy.member.login.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;

@Mapper
public interface LoginDao {
    String selectStateCode(String id);  // 회원 상태 코드 조회

    String selectIsAdmin(String id);

    void updateStateCode(String id, String member_state_code);      // 회원 상태 코드 수정

    Boolean selectIdPassword(String id, String password);   // 아이디, 비밀번호 확인

    int selectLoginCnt(String id);  // 회원 로그인 시도 횟수 조회

    void updateLoginCnt(String id); // 회원 로그인 횟수 1 증가

    void initMemInfo(String id);    // 회원 정보 초기화(로그인 카운트 0, 상태 코드 40001)
}
