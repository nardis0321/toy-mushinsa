package com.toyproject2_5.musinsatoy.CS.Notice.service;

import com.toyproject2_5.musinsatoy.CS.Notice.dto.NoticeDto;
import com.toyproject2_5.musinsatoy.CS.Notice.repository.NoticeDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class NoticeServiceImplTest {
    @Autowired
    NoticeService noticeService;
    @Autowired
    NoticeDao noticeDao;

    //0.service 정상 주입 테스트
    @Test
    @Transactional
    void service_DITest_Success() {
        assertTrue(noticeService != null);
        System.out.println("noticeService = " + noticeService);
    }

    //1. insert()
    //1-1. 공지 Dto 생성 후 삽입
    @Test
    @Transactional
    void insert_one_notice_test_success() throws Exception {
        //1)db 전체 삭제
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2)DTO 한 개 생성 후 db 삽입
        NoticeDto tempDto = new NoticeDto("testing1", "testing content", "11", "Y", "Y");
        assertTrue(noticeService.insert(tempDto) == 1); //DAO에 한 개 삽입, 검증

        //3)db 데이터 갯수 1개인지 확인
        assertTrue(noticeService.getCount() == 1);

        //TODO: db에서 불러와서 내용확인 부분 추가
    }

    //1-2. 공지 Dto N개 생성 후 삽입
    @Test
    @Transactional
    public void insert_N_notice_test_success() throws Exception {
        //1) db 전체 삭제
        noticeDao.deleteAll(); //db 전체 삭제
        assertTrue(noticeService.getCount() == 0); //DAO 내용 없는지 확인

        //2) DTO 천개 생성, db 삽입
        int testNum = 1000;
        for (int i = 1; i <= testNum; i++) {
            NoticeDto tempDto = new NoticeDto("testing"+i, "testing content", "11", "N", "Y");
            assertTrue(noticeService.insert(tempDto) == 1); //Db에 삽입, 검증
        }

        //3)db 데이터 1000개인지 검증
        assertTrue(noticeDao.count() == testNum);
    }

    //1-3. N개의 DTO insert 후 notice_id값 auto_increment와 동일한가
    @Test
    @Transactional
    public void insert_N_comparison_test_success() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2)DTO 1000번 생성, DB에 1000번 넣기
        int testNum = 1000;
        for (int i = 1; i <= testNum; i++) {
            //DTO 생성
            NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
            //DTO 삽입
            assertTrue(noticeService.insert(tempDto) == 1);
        }

        //3)db 데이터 갯수와 일치한지확인
        assertTrue(noticeDao.count() == testNum);

        //4)리스트 1000번째 DTO의 notic_id가 1000인가 assert
        List<NoticeDto> tempList = noticeDao.selectAllAsc();
        assertTrue(tempList.size() == testNum);

        Long testLong = Long.valueOf(testNum);
        assertTrue(tempList.get(999).getNotice_id().equals(testLong));
    }

    //1-4. 도메인 범위 넘어가는 값 입력시(title)
    @Test
    @Transactional
    public void insert_domain_over_test_exception() throws Exception {
        //1)db 전체 삭제
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2)DTO 한 개 생성 후 db 삽입
        NoticeDto tempDto = new NoticeDto("tttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt", "testing content", "11", "Y", "Y");
        assertThrows(DataIntegrityViolationException.class, () -> noticeService.insert(tempDto));
    }

    //1-5. dto를 넣지 않고 null을 넘길때 - NullPointerException
    @Test
    @Transactional
    public void insert_null_test_exception() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeService.getCount() == 0);

        //3)insert수행, 검증
        assertThrows(NullPointerException.class, () -> noticeService.insert(null));
    }

    //1-6. is_top_post가 null인 dto 삽입시 is_top_post가 N으로 설정되는가
    @Test
    @Transactional
    public void insert_is_top_post_null_N_test_success() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeService.getCount() == 0);

        //2) is_top_post = N인 dto 생성
        NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", null, "Y");
        //3) dto 삽입
        assertTrue(noticeService.insert(tempDto) == 1);

        List<NoticeDto> tempList = noticeDao.selectAllAsc();
        assertTrue(tempList.size() == 1);

        //4) 0번째 가져와서 is_top_post가 N인지 확인
        assertTrue(tempList.get(0).getIs_top_post().equals("N"));
    }

    //2.selectById()
    //2-1. testNum개의 Dto 삽입 후 마지막 id와 auto_increment 같은지, 같으면 정상적으로 조회되는지
    @Test
    @Transactional
    public void selectById_Success() throws Exception {
        //1) db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2) db에 100개 data 넣기
        int testNum = 100;
        for (int i = 1; i <= testNum; i++) {
            //DTO 생성
            NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
            //DTO 삽입
            assertTrue(noticeService.insert(tempDto) == 1);
        }
        //100개 맞는지 확인
        assertTrue(noticeDao.count() == testNum);

        //3) id값으로 마지막 1개 찾기(noticeService에 오름차순으로 listing하는 메서드가 없다.)
        Long testLong = Long.valueOf(testNum);
        NoticeDto resDto = noticeService.selectById(testLong);

        //4) id가 100인지 확인
        assertTrue(resDto.getNotice_id().equals(testLong));
    }

    //2-2. 존재하지 않는 notice_id로 조회할때 null이 return 되는가
    @Test
    @Transactional
    public void selectById_IdNotExist_test_Null() throws Exception {
        //1) db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2) db에 100개 data 넣기
        int testNum = 100;
        for (int i = 1; i <= testNum; i++) {
            //DTO 생성
            NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
            //DTO 삽입
            assertTrue(noticeService.insert(tempDto) == 1);
        }
        assertTrue(noticeDao.count() == testNum);

        //3) 마지막 id 값 + 1로 없는 데이터 찾기
        Long testLong = Long.valueOf(testNum);
        assertNull(noticeService.selectById(testLong+1));
    }

    //2-3. null로 찾을때 null 반환
    @Test
    @Transactional
    public void selectById_Null_Null() throws Exception {
        //1) db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2) db에 100개 data 넣기
        int testNum = 100;
        for (int i = 1; i <= testNum; i++) {
            //DTO 생성
            NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
            //DTO 삽입
            assertTrue(noticeService.insert(tempDto) == 1);
        }
        assertTrue(noticeDao.count() == testNum);

        //3) null값으로 데이터 찾기
        assertNull(noticeService.selectById(null));
    }

    //3. selectAllDescPage 테스트
    //3-1. pagination 적용시킨후 db에서 데이터를 가져왔을때 pageSize의 크기와 갯수가 같은지
    @Test
    @Transactional
    public void selectAllDescPage_Test_success() throws Exception {
        //1) db비우고
        noticeDao.deleteAll();
        assertTrue(noticeService.getCount() == 0);

        //2) 125개 넣는다
        int testNum = 125;
        for(int i = 1; i <= testNum; i++) {
            NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "N", "Y");
            assertTrue(noticeService.insert(tempDto) == 1);
        }
        assertTrue(noticeService.getCount() == testNum);

        //3) 총 게시글수 파악
        int totalCnt = noticeService.getCount();
        assertTrue(totalCnt == testNum);

        //4) map 생성
        Map map = new HashMap();
        Integer page = 1;
        Integer pageSize = 10;
        map.put("offset", (page - 1) * pageSize);
        map.put("pageSize", pageSize);

        //첫페이지 가져왔을때 pageSize와 크기가 같은지 확인
        List resList = noticeService.selectAllDescPage(map);
        assertTrue(resList.size() == pageSize);
    }

    //4. selectAllTopPost 테스트
    //4-1. 10개 중 is top post 3개 Y, 7개 N 설정, 제대로 가져오는지
    @Test
    @Transactional
    public void selectAllTopPost_test_success() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeService.getCount() == 0);

        //2)DTO 10생성, DB에 10번 넣기 3,6,9번째만 Y, 나머지 N
        int testNum = 10;
        for (int i = 1; i <= testNum; i++) {
            if(i % 3 == 0) {
                NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
                assertTrue(noticeService.insert(tempDto) == 1);
            } else {
                NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "N", "Y");
                assertTrue(noticeService.insert(tempDto) == 1);
            }
        }
        assertTrue(noticeService.getCount() == testNum); //db데이터 10개인지 확인

        //4)selectAllTopPost로 불러와서 3개인지 확인
        assertTrue(noticeService.selectAllTopPost().size() == 3);
    }

    //5. Update 테스트
    //5-1. 데이터 하나 변경하고 변경됐는지 내용확인
    @Test
    @Transactional
    public void update_test_success() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeService.getCount() == 0);

        //2)is_post=y DTO 1개생성, DB에 1번 삽입
        NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
        assertTrue(noticeService.insert(tempDto) == 1); //1개 삽입했는지 확인
        assertTrue(noticeService.getCount() == 1); //db데이터 1개인지 확인

        Long noticeId = noticeService.selectAllTopPost().get(0).getNotice_id(); //첫번째 noticeId 저장 - 내림차순

        //3)업데이트용 dto 생성 - notice_content변경
        sleep(2000); //2초 텀 주기
        String nowStr = LocalDateTime.now().toString(); //현재시간 생성
        NoticeDto updateDto = new NoticeDto(noticeId, "testing", "testing content - updated", nowStr, "12",  "Y", "Y");

        //4)update수행
        assertTrue(noticeService.update(updateDto) == 1); //update 제대로 수행되는지
        assertTrue(noticeService.getCount() == 1); //db에 그대로 data 1개인지

        //5)내용이 동일한지 확인
        assertTrue(noticeService.selectById(noticeId).getNotice_content().equals("testing content - updated"));
    }

    //5-2. not null에 null 주고 update해보기
    @Test
    @Transactional
    public void update_null_exception() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeService.getCount() == 0);

        //2)is_post=y DTO 1개생성, DB에 1번 삽입
        NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
        assertTrue(noticeService.insert(tempDto) == 1); //1개 삽입했는지 확인
        assertTrue(noticeService.insert(tempDto) == 1); //1개 삽입했는지 확인
        assertTrue(noticeService.getCount() == 2); //db데이터 2개인지 확인

        Long noticeId = noticeDao.selectAllTopPost().get(0).getNotice_id(); //첫번째 noticeId 저장

        //3)업데이트용 dto 생성 - title not null인데 null값을 준다.
        sleep(2000); //2초 텀 주기
        String nowStr = LocalDateTime.now().toString(); //현재시간 생성
        NoticeDto updateDto = new NoticeDto(noticeId, null, "testing content - updated", nowStr, "12",  "Y", "Y");

        //4)update수행
        assertThrows(DataIntegrityViolationException.class, () -> noticeService.update(updateDto));
    }

    //5-3. db에 없는 데이터 update해보기 - 0반환하고 끝 update X
    @Test
    @Transactional
    public void update_nonExist_success() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeService.getCount() == 0);

        //2)is_post=y DTO 1개생성, DB에 1번 삽입
        NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
        assertTrue(noticeService.insert(tempDto) == 1); //1개 삽입했는지 확인
        assertTrue(noticeService.insert(tempDto) == 1); //1개 삽입했는지 확인
        assertTrue(noticeService.getCount() == 2); //db데이터 2개인지 확인

        //내림차순이라 첫번째 데이터의 id값이 제일 크다
        Long lastId = noticeService.selectAllTopPost().get(0).getNotice_id();

        //3)업데이트용 dto 생성 - is_top_post=Y, 첫번째 데이터 id값 +1으로 찾음
        sleep(2000); //2초 텀 주기
        String nowStr = LocalDateTime.now().toString(); //현재시간 생성
        NoticeDto updateDto = new NoticeDto(lastId + 100, "testing-updated", "testing content - updated", nowStr, "12", "Y", "Y");

        //4)update수행
        assertTrue(noticeService.update(updateDto) == 0);
    }

    //5-4. updateDto의 도메인 범위 벗어날때
    @Test
    @Transactional
    public void update_domain_over_test_success() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeService.getCount() == 0);

        //2)is_post=y DTO 1개생성, DB에 1번 삽입
        NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
        assertTrue(noticeService.insert(tempDto) == 1); //1개 삽입했는지 확인
        assertTrue(noticeService.getCount() == 1); //db데이터 1개인지 확인

        Long noticeId = noticeService.selectAllTopPost().get(0).getNotice_id(); //첫번째 noticeId 저장 - 내림차순

        //3)업데이트용 dto 생성 - notice_content변경
        sleep(2000); //2초 텀 주기
        String nowStr = LocalDateTime.now().toString(); //현재시간 생성
        NoticeDto updateDto = new NoticeDto(noticeId, "tttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt", "testing content - updated", nowStr, "12",  "Y", "Y");

        //4)update수행 - DataIntegrityViolationException
        assertThrows(DataIntegrityViolationException.class, () -> noticeService.insert(updateDto));
        assertTrue(noticeService.getCount() == 1); //db에 그대로 data 1개인지
    }

    //6. 삭제 - is_post가 N으로 정상 update 되는지
    @Test
    @Transactional
    public void delete_test_success() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeService.getCount() == 0);

        //2)is_post=y DTO 1개생성, DB에 1번 삽입
        NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
        assertTrue(noticeService.insert(tempDto) == 1); //1개 삽입했는지 확인
        assertTrue(noticeService.insert(tempDto) == 1); //1개 삽입했는지 확인
        assertTrue(noticeService.getCount() == 2); //db데이터 2개인지 확인

        Long noticeId = noticeService.selectAllTopPost().get(0).getNotice_id(); //첫번째 noticeId 저장 - 내림차순

        //4)delete수행
        String modifier_id = "Admin";
        assertTrue(noticeService.delete(noticeId, modifier_id) == 1); //delete 제대로 수행됐는지
        assertTrue(noticeService.getCount() == 2); //db에 그대로 data 2개인지

        //5)해당 id로 찾고 is_post가 N인지 확인
        assertTrue(noticeService.selectById(noticeId).getIs_post().equals("N"));

        //6)selectAllTopPost로 1개인지 확인 - is_top_post=y가 조건이므로
        assertTrue(noticeService.selectAllTopPost().size()==1);
    }

    //6-1. 없는 id로 삭제하려는 경우
    @Test
    @Transactional
    public void delete_non_exist_exception() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeService.getCount() == 0);

        //2)is_post=y DTO 1개생성, DB에 1번 삽입
        NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
        assertTrue(noticeService.insert(tempDto) == 1); //1개 삽입했는지 확인
        assertTrue(noticeService.getCount() == 1); //db데이터 1개인지 확인

        Long noticeId = noticeService.selectAllAsc().get(0).getNotice_id(); //첫번째 noticeId 저장

        //4)delete수행 - 없는 id
        String modifier_id = "Admin";
        assertThrows(NullPointerException.class, () -> noticeService.delete(noticeId+1, modifier_id));
    }

    //6-2. 삭제 null값 주고 실행
    @Test
    @Transactional
    public void delete_null_exception_test() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeService.getCount() == 0);

        //2)id 없이 삭제, NullPointerException 예상
        String modifier_id = "Admin";
        assertThrows(NullPointerException.class, () -> noticeService.delete(null, modifier_id));
    }
}