package com.toyproject2_5.musinsatoy.CS.FAQ.repository;

import com.toyproject2_5.musinsatoy.CS.FAQ.dto.FaqDto;
import com.toyproject2_5.musinsatoy.ETC.FaqSearchCondition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FaqDaoImplTest {
    @Autowired
    FaqDao faqDao;

    //0. DAO 정상 주입 테스트
    @Test
    @Transactional
    void dao_DITest_Success() {
        assertTrue(faqDao != null);
        System.out.println("faqDao = " + faqDao);
    }

    //1.count 테스트
    //1-1. N번 넣었을때 N개 들어갔는지
    //1) db 비우기
    //2) 비교 리스트 생성
    //3) DTO N개 생성, DB에 N번 넣기
    //4) DB에서 selectlist해서 리스트에 저장
    //5) after과 before 리스트의 사이즈 비교
    //6) 마지막 값이 같은지 확인
    //7) 같은거 확인됐으면 count() 값과 같은지 비교
    @Test
    @Transactional
    public void count_N_comparison_test_success() throws Exception {
        //1)db 비우기
        faqDao.deleteAll();
        assertTrue(faqDao.count() == 0);

        //2)DTO 1000번 생성, DB에 1000번 넣기
        int testNum = 1000;
        for (int i = 1; i <= testNum; i++) {
            //DTO 생성
            FaqDto tempDto = new FaqDto("30001", 1, "Testing_FAQ", "Testing_FAQ_Content", "11", "Y");

            //DTO 삽입
            assertTrue(faqDao.insert(tempDto) == 1);
        }

        //3)DB 내용 1000개 list에 저장
        List<FaqDto> tempList = faqDao.selectAllAsc();
        assertTrue(tempList.size() == 1000);

        //4)count()의 값과 list.size()의 값이 같은가
        assertTrue(faqDao.count() == tempList.size());
    }

    //2.insertDto 테스트
    //2-1. DTO대신 null insert하려고 할때
    //2-2. DTO not null에 null값 줬을때
    //2-3. 도메인 크기 넘어가는 데이터 insert

    //3. selectById() 테스트
    //3-1. id값으로 제대로 가져오는지
    //1) db 비우기
    //2) DTO 한개 삽입하고 리스트에 저장
    //3) db 저장후 selectlist로 전체 가져와서 리스트에 저장
    //4) list size() 불러오기
    //5) list 0번째 데이터 가져와서 selectById() 값과 같은지 확인

    @Test
    @Transactional
    public void selectById_test_success() throws Exception {
        FaqDto tempDto = faqDao.selectById(1);
        System.out.println("tempDto = " + tempDto);
    }

    //4. selectAllCatDesc
    //4-1. N개 입력후 list 제대로 들고오는지
    //4-2. N개 입력후 N번째 데이터 공통코드와 카테고리명 같은지
    //4-3. 데이터 없을때

    @Test
    @Transactional
    public void selectAllCatDesc_test_success() throws Exception {
        List<FaqDto> faqList = faqDao.selectAllCatDesc();
        faqList.forEach(faqDto -> System.out.println("faqDto = " + faqDto));
    }

    //4-1. selectAllAsc() 테스트
    @Test
    @Transactional
    public void selectAllAsc_test_success() throws Exception {
        List<FaqDto> faqList = faqDao.selectAllAsc();
        faqList.forEach(faqDto -> System.out.println("faqDto = " + faqDto));
    }

    //5.selectAllCatCodeDesc 테스트
    //5-1. N개 입력후 category_code로 필터링한 데이터 제대로 들고오는지
    //5-2. 한개 입력 후 category_code와 카테고리명이 제대로 매치되는지
    //5-3. 없는 카테고리 코드로 필터링하려고 할때
    //5-4. 카테고리 코드로 null값 줬을때

    @Test
    @Transactional
    public void selectAllCatCodeDesc_test_success() throws Exception {
        //1)db 비우기
        faqDao.deleteAll();
        assertTrue(faqDao.count() == 0);

        //2)DTO생성, DB넣기
        FaqDto tempDto1 = new FaqDto("30001", 1, "Testing_FAQ1", "Testing_FAQ_Content", "11", "Y");
        assertTrue(faqDao.insert(tempDto1) == 1);
        FaqDto tempDto2 = new FaqDto("30001", 2, "Testing_FAQ2", "Testing_FAQ_Content", "11", "Y");
        assertTrue(faqDao.insert(tempDto2) == 1);
        FaqDto tempDto3 = new FaqDto("30002", 1, "Testing_FAQ3", "Testing_FAQ_Content", "11", "Y");
        assertTrue(faqDao.insert(tempDto3) == 1);
        assertTrue(faqDao.count() == 3);

        //대분류 코드 30001인 것만 가져와서 리스트
        List<FaqDto> tempList = faqDao.selectAllCatCodeDesc("30001");
        tempList.forEach(System.out::println);
    }

    //6.selectAllCatCodeSubIdDesc 테스트
    //6-1. N개 입력후 category_code와 subcategory_id로 필터링한 데이터 제대로 들고오는지
    //6-2. 한개 입력 후 subcategory_id와 서브카테고리명이 제대로 매치되는지
    //6-3. 없는 subcategory_id로 필터링하려고 할때
    //6-4. subcategory_id로 null값 줬을때
    @Test
    @Transactional
    public void selectAllCatCodeSubIdDesc_test_success() throws Exception {
        //1)db 비우기
        faqDao.deleteAll();
        assertTrue(faqDao.count() == 0);

        //2)DTO생성, DB넣기
        FaqDto tempDto1 = new FaqDto("30001", 1, "Testing_FAQ1", "Testing_FAQ_Content", "11", "Y");
        assertTrue(faqDao.insert(tempDto1) == 1);
        FaqDto tempDto2 = new FaqDto("30001", 2, "Testing_FAQ2", "Testing_FAQ_Content", "11", "Y");
        assertTrue(faqDao.insert(tempDto2) == 1);
        FaqDto tempDto3 = new FaqDto("30002", 1, "Testing_FAQ3", "Testing_FAQ_Content", "11", "Y");
        assertTrue(faqDao.insert(tempDto3) == 1);
        assertTrue(faqDao.count() == 3);

        //Map생성
        Map map = new HashMap();
        map.put("faq_category_code", "30001");
        map.put("faq_subcategory_id", 2);

        //대분류 코드 30001인 것만 가져와서 리스트
        List<FaqDto> tempList = faqDao.selectAllCatCodeSubIdDesc(map);
        tempList.forEach(System.out::println);
    }

    //7.update 테스트
    //7-1. 한개 제대로 update했을때
    //1) 업데이트 하려던 dto값이랑 업데이트 한 dto 값이랑 같은지 확인
    //7-2. 없는 데이터 update하려고 할때
    //7-3. update에 null값 주기

    //8.deleteById테스트
    //8-1. 한개 제대로 삭제하기
    //8-2. 없는 데이터 삭제하려고 할때
    //8-3. id 값으로 null주기

    //9.deleteAll테스트
    //9-1. N개 넣고 모두 삭제하고 리스트에 selectList해서 0번째 값이 있는지 assertNull


    //10. searchSelect() 테스트
    @Test
    @Transactional
    public void searchSelect_test_success() throws Exception {
        //1)db 비우기
        faqDao.deleteAll();
        assertTrue(faqDao.count() == 0);

        //2)DTO생성, DB넣기
        FaqDto tempDto1 = new FaqDto("30001", 1, "Testing_FAQ", "Testing_Content1", "11", "Y");
        assertTrue(faqDao.insert(tempDto1) == 1);
        FaqDto tempDto2 = new FaqDto("30001", 2, "Testing_FAQ", "Testing_Content2", "11", "Y");
        assertTrue(faqDao.insert(tempDto2) == 1);
        FaqDto tempDto3 = new FaqDto("30002", 1, "Testing_FAQ", "Testing_Content3", "11", "Y");
        assertTrue(faqDao.insert(tempDto3) == 1);
        assertTrue(faqDao.count() == 3);


        FaqSearchCondition fsc = new FaqSearchCondition("Content3");
        List<FaqDto> tempList = faqDao.searchSelect(fsc);
        System.out.println("tempList.size() = " + tempList.size());
        tempList.forEach(System.out::println);

        //대분류 코드 30001인 것만 가져와서 리스트
    }
    //11. searchResultCnt() 테스트
    //1000개 생성해서 넣고 100마다 타이틀에 hello를 넣는다.
    //10개면 성공
    @Test
    @Transactional
    public void searchResultCnt_test_success() throws Exception {
        faqDao.deleteAll();
        assertTrue(faqDao.count() == 0);

        int testNum = 1000;
        for(int i = 1; i <= testNum; i++) {
            if(i % 100 == 0) {
                FaqDto tempDto = new FaqDto("30001", 1, "Testing_FAQ" + i + "hello", "Testing_Content"+i, "11", "Y");
                assertTrue(faqDao.insert(tempDto) == 1);
            } else {
                FaqDto tempDto = new FaqDto("30001", 1, "Testing_FAQ" + i, "Testing_Content"+i, "11", "Y");
                assertTrue(faqDao.insert(tempDto) == 1);
            }
        }
        assertTrue(faqDao.count() == testNum);

        FaqSearchCondition fsc = new FaqSearchCondition("hello");
        int countSearch = faqDao.searchResultCnt(fsc);
        System.out.println("countSearch = " + countSearch);
    }
}