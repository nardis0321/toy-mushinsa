package com.toyproject2_5.musinsatoy.member.login.service;

import com.toyproject2_5.musinsatoy.member.join.repository.AdminBrandDao;
import com.toyproject2_5.musinsatoy.member.join.repository.MemberDao;
import com.toyproject2_5.musinsatoy.member.login.repository.LoginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
    @Autowired
    LoginDao loginDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    AdminBrandDao adminBrandDao;

    // 로그인
    public Object loginCheck(String id, String password){

        // 회원 상태 코드 확인
        String stateCode = loginDao.selectStateCode(id);

        // stateCode가 null인 경우 예외 던지기
        if (stateCode == null) {
            //throw new IllegalStateException("존재하지 않는 회원입니다.");
            return "존재하지 않는 회원입니다.";
        }

        // 회원 상태 코드에 따른 처리
        switch (stateCode) {
            case "40001": // 정상 회원
                if (loginDao.selectLoginCnt(id) >= 3) { // 비밀번호 3번 이상 틀리면
                    loginDao.updateStateCode(id, "40003"); // 상태 코드 '잠금'으로 변경
                    return "비밀번호를 3회 이상 잘못 입력하여 계정이 잠겼습니다.";
                }
                if (loginDao.selectIdPassword(id, password)) { // 로그인 성공
                    loginDao.initMemInfo(id); // 로그인 시도 횟수, 상태 코드 초기화
                    // 회원이 admin인지 판단하기
                    String is_admin = loginDao.selectIsAdmin(id);
                    if(is_admin==null)
                        return "존재하지 않는 회원입니다.";
                    Map result = checkAdmin(id, is_admin);
                    return result;
                } else {
                    loginDao.updateLoginCnt(id); // 비밀번호 틀리면 로그인 카운트 증가
                    return "아이디 또는 비밀번호가 일치하지 않습니다.";
                }

            case "40002": // 휴면 상태
                return "회원이 휴면 상태입니다.";

            case "40003": // 잠금 상태
                return "회원이 잠금 상태입니다.";

//            case "40004": // 탈퇴 상태
//                return "회원이 탈퇴 상태입니다: ID=" + id;

            default:
                return "존재하지 않는 회원입니다.";
        }

    }

    public Map checkAdmin(String id, String is_admin){
        Map map = new HashMap();

        switch (is_admin) {
            case "U": // 일반 회원이면
                map.put("isAdmin", "U");
                map.put("brand", "NONE");
                return map;

            case "A":// 홈페이지 admin이면
                map.put("isAdmin", "A");
                map.put("brand", "NONE");
                return map;

            case "B":// brand admin이면
                map.put("isAdmin", "B");
                String brand = adminBrandDao.selectAdminBrand(id).getBrand_code();
                map.put("brand", brand);
                return map;
        }
        return null;
    }

}
