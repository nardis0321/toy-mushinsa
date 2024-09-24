package com.toyproject2_5.musinsatoy.member.join.repository;

import com.toyproject2_5.musinsatoy.member.join.dto.MemberDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberDaoTest {
    @Autowired
    MemberDao memberDao;

    /* deleteAll Test */
    @Test
    public void deleteAllTest(){
        memberDao.deleteAll();
        assertTrue(memberDao.count() == 0);

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

        memberDao.deleteAll();
        assertTrue(memberDao.count() == 0);
    }

    /* insertTest */
    @Test
    public void insertMemberTest(){
        memberDao.deleteAll();
        assertTrue(memberDao.count() == 0);

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

        assertTrue(memberDao.insertMember(memberDto)==1);
        assertTrue(memberDao.count() == 2);

    }

    /* selectOne Test */
    @Test
    public void selectOneTest(){
        memberDao.deleteAll();
        assertTrue(memberDao.count() == 0);

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

        assertTrue(memberDao.selectOne(memberDto.getId()).getId().equals(memberDto.getId()));

        MemberDto memberDto2 = new MemberDto(
                "qwert1234"
                , "qwert1234!"
                , "우수빈"
                ,"1992-03-31"
                , "F"
                , "01045678901"
                , "qwert@naver.com"
                , "asdf1234"
                ,"U"
        );
        assertTrue(memberDao.insertMember(memberDto2)==1);
        assertTrue(memberDao.count()==2);

        assertTrue(memberDao.selectOne(memberDto2.getId()).getId().equals(memberDto2.getId()));

    }
}