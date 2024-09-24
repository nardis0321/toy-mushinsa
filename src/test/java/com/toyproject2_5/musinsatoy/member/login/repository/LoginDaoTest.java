package com.toyproject2_5.musinsatoy.member.login.repository;

import com.toyproject2_5.musinsatoy.member.join.dto.MemberDto;
import com.toyproject2_5.musinsatoy.member.join.repository.MemberDao;
import com.toyproject2_5.musinsatoy.member.login.dto.LoginDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginDaoTest {
    @Autowired
    LoginDao loginDao;

    @Autowired
    MemberDao memberDao;

    /* 회원 데이터 생성 */
    @Test
    public void createData(){
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
                ,"B"
        );
        assertTrue(memberDao.insertMember(memberDto)==1);
        assertTrue(memberDao.count() == 1);

        //loginDao.updateStateCode(memberDto.getId(), "40004");
    }

    /* selectStateCode Test */
    @Test
    public void selectStateCodeTest(){
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

        /* 1. 회원인 경우, 상태 코드 조회 성공 */
        assertTrue(loginDao.selectStateCode(memberDto.getId()).equals("40001"));

        /* 2. 비회원인 경우 상태 코드 조회 실패 */
        assertNull(loginDao.selectStateCode("qwert1234"));
    }

    /* selectIsAdmin Test */
    @Test
    public void selectIsAdminTest(){
        /* 일반회원일 때 */
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
        assertEquals(1,memberDao.insertMember(memberDto));
        assertEquals(1, memberDao.count());

        assertEquals("U", loginDao.selectIsAdmin(memberDto.getId()));


        /* 홈페이지 Admin일 때, */
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
        assertEquals(1,memberDao.insertMember(memberDto));
        assertEquals(1, memberDao.count());

        assertEquals("A", loginDao.selectIsAdmin(memberDto.getId()));


        /* 브랜드 admin 때 */
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
        assertEquals(1,memberDao.insertMember(memberDto));
        assertEquals(1, memberDao.count());

        assertEquals("B", loginDao.selectIsAdmin(memberDto.getId()));
    }


    /* updateStateCode Test */
    @Test
    public void updateStateCodeTest(){
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
        assertTrue(loginDao.selectStateCode(memberDto.getId()).equals("40001"));

        // 회원 상태 코드 수정
        loginDao.updateStateCode(memberDto.getId(), "40002");
        assertTrue(loginDao.selectStateCode(memberDto.getId()).equals("40002"));
    }

    /* selectIdPassword Test */
    @Test
    public void selectIdPasswordTest(){
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

        /* 1. 로그인 성공 */
        assertTrue(loginDao.selectIdPassword(memberDto.getId(), memberDto.getPassword()));

        /* 2. 비밀번호 틀릴 경우, 로그인 실패 */
        memberDao.deleteAll();
        assertTrue(memberDao.count()==0);
        assertTrue(memberDao.insertMember(memberDto)==1);
        assertTrue(memberDao.count() == 1);

        assertFalse(loginDao.selectIdPassword(memberDto.getId(),"qwert1234!"));

        /* 3. 비회원 로그인 실패 */
        memberDao.deleteAll();
        assertTrue(memberDao.count()==0);
        assertTrue(memberDao.insertMember(memberDto)==1);
        assertTrue(memberDao.count() == 1);

        assertNull(loginDao.selectIdPassword("qwert1234",memberDto.getPassword()));
    }

    /* selectLoginCnt Test */
    @Test
    public void selectLoginCntTest(){
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
        assertTrue(loginDao.selectLoginCnt(memberDto.getId())==0);

        /* 1. 회원 일 때, 비밀번호 틀림 -> 로그인 시도 횟수 증가 */
        memberDao.deleteAll();
        assertTrue(memberDao.count()==0);
        assertTrue(memberDao.insertMember(memberDto)==1);
        assertTrue(memberDao.count() == 1);

        assertTrue(!loginDao.selectIdPassword(memberDto.getId(),"qwert1234!"));
    }

    /* updateLoginCnt Test */
    @Test
    public void updateLoginCntTest(){
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

        assertTrue(loginDao.selectLoginCnt(memberDto.getId())==0);

        // 로그인 카운트 증가
        loginDao.updateLoginCnt(memberDto.getId());
        assertTrue(loginDao.selectLoginCnt(memberDto.getId())==1);
        loginDao.updateLoginCnt(memberDto.getId());
        assertTrue(loginDao.selectLoginCnt(memberDto.getId())==2);
    }

    /* initMemInfo Test */
    @Test
    public void initMemInfoTest(){
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

        assertTrue(loginDao.selectLoginCnt(memberDto.getId())==0);

        // 로그인 카운트 증가
        loginDao.updateLoginCnt(memberDto.getId());
        assertTrue(loginDao.selectLoginCnt(memberDto.getId())==1);

        // 회원 정보 초기화(로그인 횟수 0, 회원 상태 코드 40001)
        loginDao.initMemInfo(memberDto.getId());
        assertTrue(loginDao.selectStateCode(memberDto.getId()).equals("40001"));
        assertTrue(loginDao.selectLoginCnt(memberDto.getId())==0);
    }


}