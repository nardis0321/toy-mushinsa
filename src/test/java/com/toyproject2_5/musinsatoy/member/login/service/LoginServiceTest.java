package com.toyproject2_5.musinsatoy.member.login.service;

import com.toyproject2_5.musinsatoy.member.join.dto.AdminBrandDto;
import com.toyproject2_5.musinsatoy.member.join.dto.MemberDto;
import com.toyproject2_5.musinsatoy.member.join.repository.MemberDao;
import com.toyproject2_5.musinsatoy.member.login.repository.LoginDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginServiceTest {
    @Autowired
    LoginService loginService;
    @Autowired
    LoginDao loginDao;
    @Autowired
    MemberDao memberDao;

    /* loginCheck Test */
    @Test
    public void loginloginCheckTest(){
        memberDao.deleteAll();
        assertTrue(memberDao.count()==0);

        MemberDto memberDto = new MemberDto(
                "asdf1234"
                , "asdf1234!"
                , "김주홍"
                ,"1990-02-20"
                , "M"
                , "01098765432"
                , "asdf@naver.com"
                , ""
                ,"U"
        );
        assertTrue(memberDao.insertMember(memberDto)==1);
        assertTrue(memberDao.count() == 1);

        /* 1. 회원 로그인 성공 */
        assertEquals("success",loginService.loginCheck(memberDto.getId(), memberDto.getPassword()));

        /* 2. 회원 로그인 1회 실패 후, 로그인 성공 */
        memberDao.deleteAll();
        assertTrue(memberDao.count()==0);
        assertTrue(memberDao.insertMember(memberDto)==1);
        assertTrue(memberDao.count() == 1);

        assertEquals("fail", loginService.loginCheck(memberDto.getId(), "qwert1234!"));
        assertTrue(loginDao.selectLoginCnt(memberDto.getId())==1);
        assertEquals("success", loginService.loginCheck(memberDto.getId(), memberDto.getPassword()));
        assertTrue(loginDao.selectLoginCnt(memberDto.getId())==0);


        /* 3. 회원 로그인 3회 실패 후, 로그인 실패 */
        memberDao.deleteAll();
        assertTrue(memberDao.count()==0);
        assertTrue(memberDao.insertMember(memberDto)==1);
        assertTrue(memberDao.count() == 1);

        assertEquals("fail", loginService.loginCheck(memberDto.getId(), "qwert1234!"));
        assertTrue(loginDao.selectLoginCnt(memberDto.getId())==1);
        assertEquals("fail", loginService.loginCheck(memberDto.getId(), "qwert1234!"));
        assertTrue(loginDao.selectLoginCnt(memberDto.getId())==2);
        assertEquals("fail", loginService.loginCheck(memberDto.getId(), "qwert1234!"));
        assertTrue(loginDao.selectLoginCnt(memberDto.getId())==3);
        assertEquals("fail", loginService.loginCheck(memberDto.getId(), "qwert1234!"));
        assertTrue(loginDao.selectLoginCnt(memberDto.getId())==3);
        assertEquals("40003", loginDao.selectStateCode(memberDto.getId()));


        /* 4. 없는 아이디 예외 발생  */
        memberDao.deleteAll();
        assertTrue(memberDao.count()==0);
        assertTrue(memberDao.insertMember(memberDto)==1);
        assertTrue(memberDao.count() == 1);

        IllegalStateException thrownException = assertThrows(IllegalStateException.class, () -> {
            loginService.loginCheck("qwert1234", memberDto.getPassword());
        });
        // 예외 메시지 확인
        assertEquals("회원 상태 코드가 존재하지 않습니다: ID=qwert1234", thrownException.getMessage());


        /* 5. 휴면 아이디 로그인 시도 실패 */
        memberDao.deleteAll();
        assertTrue(memberDao.count()==0);
        assertTrue(memberDao.insertMember(memberDto)==1);
        assertTrue(memberDao.count() == 1);
        loginDao.updateStateCode(memberDto.getId(), "40002"); // 휴면 상태로 변경

        thrownException = assertThrows(IllegalStateException.class, () -> {
            loginService.loginCheck(memberDto.getId(), memberDto.getPassword());
        });
        // 예외 메시지 확인
        assertEquals("회원이 휴면 상태입니다: ID="+memberDto.getId(), thrownException.getMessage());


        /* 6. 잠금 아이디 로그인 시도 실패 */
        memberDao.deleteAll();
        assertTrue(memberDao.count()==0);
        assertTrue(memberDao.insertMember(memberDto)==1);
        assertTrue(memberDao.count() == 1);
        loginDao.updateStateCode(memberDto.getId(), "40003"); // 잠금 상태로 변경

        thrownException = assertThrows(IllegalStateException.class, () -> {
            loginService.loginCheck(memberDto.getId(), memberDto.getPassword());
        });
        // 예외 메시지 확인
        assertEquals("회원이 잠금 상태입니다: ID="+memberDto.getId(), thrownException.getMessage());


        /* 7. 탈퇴 아이디 로그인 시도 실패 */
        memberDao.deleteAll();
        assertTrue(memberDao.count()==0);
        assertTrue(memberDao.insertMember(memberDto)==1);
        assertTrue(memberDao.count() == 1);
        loginDao.updateStateCode(memberDto.getId(), "40004"); // 탈퇴 상태로 변경

        thrownException = assertThrows(IllegalStateException.class, () -> {
            loginService.loginCheck(memberDto.getId(), memberDto.getPassword());
        });
        // 예외 메시지 확인
        assertEquals("회원이 탈퇴 상태입니다: ID="+memberDto.getId(), thrownException.getMessage());
    }


    /* checkAdmin Test */
    @Test
    public void checkAdminTest(){
        memberDao.deleteAll();
        assertTrue(memberDao.count()==0);

        MemberDto memberDto = new MemberDto(
                "asdf1234"
                , "asdf1234!"
                , "김주홍"
                ,"1990-02-20"
                , "M"
                , "01098765432"
                , "asdf@naver.com"
                , ""
                ,"U"
        );
        assertEquals(1, memberDao.insertMember(memberDto));
        assertEquals(1, memberDao.count());

        /* 1. 일반 회원 조회 성공 */
        Map map = loginService.checkAdmin(memberDto.getId(), memberDto.getIs_admin());
        assertEquals("U", map.get("isAdmin"));
        assertEquals("NONE", map.get("brand"));


        /* 2. 홈페이지 관리자 조회 성공 */
        memberDao.deleteAll();
        assertTrue(memberDao.count()==0);

        memberDto = new MemberDto(
                "asdf1234"
                , "asdf1234!"
                , "김주홍"
                ,"1990-02-20"
                , "M"
                , "01098765432"
                , "asdf@naver.com"
                , ""
                ,"A"
        );
        assertEquals(1, memberDao.insertMember(memberDto));
        assertEquals(1, memberDao.count());

        /* 1. 일반 회원 조회 성공 */
        map = loginService.checkAdmin(memberDto.getId(), memberDto.getIs_admin());
        assertEquals("A", map.get("isAdmin"));
        assertEquals("NONE", map.get("brand"));


        /* 2. 홈페이지 관리자 조회 성공 */
        memberDao.deleteAll();
        assertTrue(memberDao.count()==0);

        memberDto = new MemberDto(
                "asdf1234"
                , "asdf1234!"
                , "김주홍"
                ,"1990-02-20"
                , "M"
                , "01098765432"
                , "asdf@naver.com"
                , ""
                ,"B"
        );
        assertEquals(1, memberDao.insertMember(memberDto));
        assertEquals(1, memberDao.count());

        /* 3. 브랜드 관리자 조회 성공 */
        map = loginService.checkAdmin(memberDto.getId(), memberDto.getIs_admin());
        assertEquals("B", map.get("isAdmin"));
        assertEquals("CHANEL", map.get("brand"));
    }
}