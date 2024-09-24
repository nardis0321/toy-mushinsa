package com.toyproject2_5.musinsatoy.CS.Notice.repository;

import com.toyproject2_5.musinsatoy.CS.Notice.dto.NoticeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

/*
@RequiredArgsConstructor - junit5는 사용불가
expected - junit5에서 사용 불가, assertThrow 사용
성공case-id 리스트 가져와서 비교
실패case-null, 중복id,
예외case-outofbounds
<긱각 행위에 대해 Assertion이 들어가야 함>
String nowStr = LocalDateTime.now().toString(); //현재시간
*/

//TODO : 넣기전 데이터 여러개인경우 list로 미리 뽑아놓기 / 넣기전, db에서 가져온 후 비교

@SpringBootTest
public class NoticeDaoImplTest {
    @Autowired
    NoticeDao noticeDao;

    //0.DAO 정상 주입 테스트
    @Test
    @Transactional
    void dao_DITest_Success() {
        assertTrue(noticeDao != null);
        System.out.println("noticeDao = " + noticeDao);
    }

    //TEST DATA 입력
//    @Test
//    public void insert_test_data() throws Exception {
//        //1)db 비우기
//        noticeDao.deleteAll();
//
//        //2)DTO 220개생성, DB에 220번 넣기 100번째만 상단게시, 나머지 N
//        int testNum = 220;
//        for (int i = 1; i <= testNum; i++) {
//            if(i % 100 == 0) {
//                NoticeDto tempDto = new NoticeDto("testing" + i, "testing content" + i, "20230101001", "Y","Y");
//                assertTrue(noticeDao.insert(tempDto) == 1);
//            } else {
//                NoticeDto tempDto = new NoticeDto("testing" + i, "testing content" + i, "20230101001", "N", "Y");
//                assertTrue(noticeDao.insert(tempDto) == 1);
//            }
//        }
//    }

    //1. insert 테스트
    @Test
    @Transactional
    //성공case
    //1-1. 한개 DTO insert 성공
    public void insert_one_test_success() throws Exception {
        //1)db 전체 삭제
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2)DTO 한 개 생성 후 db 삽입
        NoticeDto tempDto = new NoticeDto("testing1", "testing content", "11", "Y", "Y");
        assertTrue(noticeDao.insert(tempDto) == 1); //DAO에 한 개 삽입, 검증

        //3)db 데이터 갯수 1개인지 확인
        assertTrue(noticeDao.count() == 1);

        //4)db에서 저장된걸 가져온다.
        NoticeDto compDto = noticeDao.selectAllAsc().get(0);

        //5)저장전 dto의 값과 저장후 dto의 갑이 같은지 검증
        assertTrue(compDto.getNotice_title().equals(tempDto.getNotice_title()));
        assertTrue(compDto.getNotice_content().equals(tempDto.getNotice_content()));
        assertTrue(compDto.getWriter().equals(tempDto.getWriter()));
        assertTrue(compDto.getIs_top_post().equals(tempDto.getIs_top_post()));
    }

    @Test
    @Transactional
    //1-2. N개의 DTO insert 성공
    public void insert_N_test_success() throws Exception {
        //1) db 전체 삭제
        noticeDao.deleteAll(); //DAO 전체 삭제
        assertTrue(noticeDao.count() == 0); //DAO 내용 없는지 확인

        //2) DTO 천개 생성, db 삽입
        int testNum = 1000; //테스트 횟수 넘버
        List<NoticeDto> tempDtoList = new ArrayList<>(); //db 삽입전 dto 넣을 리스트
        List<NoticeDto> dbDtoList = new ArrayList<>(); //db에서 가져온 dto를 넣을 리스트

        for (int i = 1; i <= testNum; i++) {
            NoticeDto noticeDto = new NoticeDto("testing"+i, "testing content", "11", "Y", "Y");
            tempDtoList.add(noticeDto); //dtoList에 추가
            assertTrue(noticeDao.insert(noticeDto) == 1); //Db에 삽입, 검증
        }

        //3)Dto 리스트의 사이즈가 데이터 갯수와 같은가
        assertTrue(tempDtoList.size() == testNum);

        //4)db 데이터 1000개인지 검증
        assertTrue(noticeDao.count() == testNum);

        //5)dbDtoList에 db에서 오름차순으로 가져온 데이터 모두 저장
        dbDtoList = noticeDao.selectAllAsc();

        //6)dto리스트와 db의 마지막 데이터 가져와서 notice_title이 같은지 확인
        int tempDtoListSize = dbDtoList.size();
        int dbDtoListSize = tempDtoList.size();
        NoticeDto tempDto = tempDtoList.get(tempDtoListSize - 1);
        NoticeDto dbDto = dbDtoList.get(dbDtoListSize - 1);

        assertTrue(tempDto.getNotice_title().equals(dbDto.getNotice_title()));
        assertTrue(tempDto.getNotice_content().equals(dbDto.getNotice_content()));
        assertTrue(tempDto.getWriter().equals(dbDto.getWriter()));
        assertTrue(tempDto.getIs_top_post().equals(dbDto.getIs_top_post()));
    }

    //예외case
    //1-3. not null인데 null값 준 후 insert
    @Test
    @Transactional
    public void insert_null_test_exception() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2)dto 생성 - 제목 필수인데 null값 줌
        NoticeDto tempDto = new NoticeDto(null, "testing content", "11", "Y", "Y");

        //3)insert수행, 검증
        assertThrows(DataIntegrityViolationException.class, () -> noticeDao.insert(tempDto));

        //4)insert가 수행되지 않고 db에 값이 없는지 확인
        assertTrue(noticeDao.count() == 0);
    }

    //1-4. 도메인 크기 넘어가는 데이터 입력후 insert시 에러
    @Test
    @Transactional
    public void insert_outofbound_test_exception() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2)dto 생성 - is_top_post 1글자인데 10글자 넣기
        NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "1234567890", "Y");

        //3)insert수행, 검증
        assertThrows(DataIntegrityViolationException.class, () -> noticeDao.insert(tempDto));

        //4)insert가 수행되지 않고 db에 값이 없는지 확인
        assertTrue(noticeDao.count() == 0);
    }

    //2. count test
    //2-1. db에 1000개의 dto를 삽입하고 count()한 값, db내용 list로 가져온 후 list.size()값 같은지
    @Test
    @Transactional
    public void count_thousand_comparison_test_success() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2)비교 리스트 생성
        List<NoticeDto> tempDtoList = new ArrayList<>();
        List<NoticeDto> dbDtoList = new ArrayList<>();

        //3)DTO 1000번 생성, DB에 1000번 넣기
        for (int i = 1; i <= 1000; i++) {
            //DTO 생성
            NoticeDto noticeDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
            //리스트에 추가
            tempDtoList.add(noticeDto);
            //DTO 삽입
            assertTrue(noticeDao.insert(noticeDto) == 1);
        }

        //4)같은 데이터인지 비교
        int tempDtoListSize = dbDtoList.size();
        int dbDtoListSize = tempDtoList.size();
        NoticeDto tempDto = tempDtoList.get(tempDtoListSize - 1);
        NoticeDto dbDto = dbDtoList.get(dbDtoListSize - 1);

        assertTrue(tempDto.getNotice_title().equals(dbDto.getNotice_title()));
        assertTrue(tempDto.getNotice_content().equals(dbDto.getNotice_content()));
        assertTrue(tempDto.getWriter().equals(dbDto.getWriter()));
        assertTrue(tempDto.getIs_top_post().equals(dbDto.getIs_top_post()));

        //5)DB 내용 1000개 list에 저장
        List<NoticeDto> tempList = noticeDao.selectAllAsc();
        assertTrue(tempList.size() == 1000);

        //6)count()의 값과 list.size()의 값이 같은가
        assertTrue(noticeDao.count() == tempList.size());
    }

    //3. deleteById 테스트
    @Test
    @Transactional
    public void deleteById_Success_Test() throws Exception {
        //1) db비우고
        noticeDao.deleteAll();
        assertTrue(noticeDao.count()==0);

        //2) Dto 생성 후 db에 하나 삽입
        NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
        assertTrue(noticeDao.insert(tempDto)==1);

        //3) dao에서 0번째 dto의 id 가져온다. 해당 아이디로 찾아서 삭제후 확인
        Long noticeId = noticeDao.selectAllAsc().get(0).getNotice_id();
        assertTrue(noticeDao.deleteById(noticeId)==1); //첫번째 dto 삭제한다.
        assertTrue(noticeDao.count()==0); //db 비워져있는지 확인

        //4) dto 2번 넣고 두번째 데이터 삭제후 갯수 1개인지 확인
        assertTrue(noticeDao.insert(tempDto)==1);
        assertTrue(noticeDao.insert(tempDto)==1);
        noticeId = noticeDao.selectAllAsc().get(0).getNotice_id();
        assertTrue(noticeDao.deleteById(noticeId+1)==1);
        assertTrue(noticeDao.count()==1);
    }

    //3-1. deleteById 테스트 - 삭제후 그 값만 삭제됐는지, 다른값들은 그대로 있는지 확인

    //4. deleteAll 테스트
    @Test
    @Transactional
    public void deleteAll_Success_Test() throws Exception {
        //1)db를 비운다.
        noticeDao.deleteAll();
        assertTrue(noticeDao.count()==0);

        //2) dto 생성후 1번 삽입 1번 삭제
        NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
        assertTrue(noticeDao.insert(tempDto)==1); //하나 넣고
        assertTrue(noticeDao.deleteAll()==1); //전체 빼고
        assertTrue(noticeDao.count()==0); //0개인지

        //3)db에 1000번 넣고 db비우기
        int testCnt = 1000;
        for(int i = 1; i <= testCnt; i++){
            assertTrue(noticeDao.insert(tempDto)==1);
        }
        assertTrue(noticeDao.deleteAll()==1000); //1000번 지웠는지 확인
        assertTrue(noticeDao.count()==0); //db 데이터 없는지 확인
    }

    //5. selectById 테스트
    //성공case
    //5-1. id값으로 제대로 하나 가져올때(마지막)
    @Test
    @Transactional
    public void selectById_Success() throws Exception {
        //1) db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2) 테스트 리스트 준비
        int testNum = 100; //테스트 횟수 넘버
        List<NoticeDto> tempDtoList = new ArrayList<>(); //db 삽입전 dto 넣을 리스트

        //3) db에 100개 data 넣기
        for (int i = 1; i <= testNum; i++) {
            NoticeDto tempDto = new NoticeDto("testing"+i, "testing content", "11", "Y", "Y");
            tempDtoList.add(tempDto); //dtoList에 추가"
            assertTrue(noticeDao.insert(tempDto) == 1); //db에 삽입
        }
        assertTrue(noticeDao.count() == testNum); //db data 갯수 count

        //4) dtoList의 마지막 id 저장
        Long noticeId = tempDtoList.get(tempDtoList.size()-1).getNotice_id();

        //5) noticeId로 찾은 noticeDto의 notice_id와 비교한다.
        NoticeDto resDto = noticeDao.selectById(noticeId);

        assertTrue(resDto.getNotice_id().equals(noticeId));
    }

    //5-2. 없으면 null
    @Test
    @Transactional
    public void selectById_IdNotExist_Success() throws Exception {
        //1) db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2) db에 100개 data 넣기
        for (int i = 1; i <= 100; i++) {
            //DTO 생성
            NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");

            //DTO 삽입
            assertTrue(noticeDao.insert(tempDto) == 1);
        }
        assertTrue(noticeDao.count() == 100);

        //3) id값으로 101번째 1개 찾기
        NoticeDto resDto = noticeDao.selectById(101L);
        //4) 가져온 dto가 null인지 확인
        assertNull(resDto);
    }

    //5-3. null로 호출할때 null이 정상적으로 가져와짐
    @Test
    @Transactional
    public void selectByNull_Exception() throws Exception {
        //1) db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2) db에 5개 data 넣기
        for (int i = 1; i <= 5; i++) {
            //DTO 생성
            NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");

            //DTO 삽입
            assertTrue(noticeDao.insert(tempDto) == 1);
        }
        assertTrue(noticeDao.count() == 5);

        //3) id값으로 101번째 1개 찾기
        NoticeDto resDto = noticeDao.selectById(null);
        //4) 가져온 dto가 null인지 확인
        assertNull(resDto);
    }

    //6. selectAllDesc 테스트
    //6-1. 1000개 넣고 count 1000개인지, 마지막 id값 1인지
    @Test
    @Transactional
    public void selectAllDesc_firstlastId_comparison_test_success() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2)DTO 1000번 생성, DB에 1000번 넣기
        int testNum = 1000;
        for (int i = 1; i <= testNum; i++) {
            //DTO 생성
            NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");

            //DTO 삽입
            assertTrue(noticeDao.insert(tempDto) == 1);
        }

        //3)DB 내용 1000개 list에 저장
        List<NoticeDto> tempList = noticeDao.selectAllDesc();
        assertTrue(tempList.size() == testNum);
        assertTrue(noticeDao.count() == tempList.size());

        //4)리스트 마지막 dto 원소 id값이 1인지
        long lastId = tempList.get(tempList.size() - 1).getNotice_id();
        assertTrue(lastId == 1); //TODO

        //5)리스트 처음 dto 원소 id값이 1000인지
        Long testLong = Long.valueOf(testNum);
        System.out.println("testLong = " + testLong);
        long firstId = tempList.get(0).getNotice_id();
        assertTrue(firstId == testNum);
    }

    //7. selectAllAsc 테스트 - is_post조건 추가
    //7-1. 1000개 넣고 count 1000개인지, 마지막 id값 1인지
    @Test
    @Transactional
    public void selectAllAsc_firstlastId_comparison_test_success() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2)DTO 1000번 생성, DB에 1000번 넣기
        int testNum = 1000;
        for (int i = 1; i <= testNum; i++) {
            //DTO 생성
            NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");

            //DTO 삽입
            assertTrue(noticeDao.insert(tempDto) == 1);
        }

        //3)DB 내용 1000개 list에 저장
        List<NoticeDto> tempList = noticeDao.selectAllAsc();
        assertTrue(tempList.size() == testNum);
        assertTrue(noticeDao.count() == tempList.size());

        //4)리스트 마지막 dto 원소 id값이 1000인지
        long lastId = tempList.get(tempList.size() - 1).getNotice_id();
        Long testLong = Long.valueOf(testNum);
        assertTrue(lastId == testLong); //TODO

        //5)리스트 처음 dto 원소 id값이 1인지
        long firstId = tempList.get(0).getNotice_id();
        assertTrue(firstId == 1);
    }

    //8. selectAllTopPost 테스트 - is_post조건 추가
    //8-1. 10개 중 is top post 3개 Y, 7개 N 설정, 제대로 가져오는지
    @Test
    @Transactional
    public void selectAllTopPost_test_success() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2)DTO 10생성, DB에 10번 넣기 3,6,9번째만 Y, 나머지 N
        int testNum = 10;
        for (int i = 1; i <= testNum; i++) {
            if(i % 3 == 0) {
                NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
                assertTrue(noticeDao.insert(tempDto) == 1);
            } else {
                NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "N", "Y");
                assertTrue(noticeDao.insert(tempDto) == 1);
            }
        }
        assertTrue(noticeDao.count() == testNum); //db데이터 10개인지 확인

        //3)DB 내용 10개 list에 저장
        List<NoticeDto> tempList = noticeDao.selectAllAsc();
        assertTrue(tempList.size() == testNum); //list size 10인지

        //4)selectAllTopPost로 불러와서 3개인지 확인
        List<NoticeDto> topPostList = noticeDao.selectAllTopPost();
        assertTrue(topPostList.size() == 3);
    }

    //9. Update 테스트
    //9-1. 데이터 하나 변경하고 변경됐는지 확인
    @Test
    @Transactional
    public void update_count_test_success() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2)is_post=y DTO 1개생성, DB에 1번 삽입
        NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
        assertTrue(noticeDao.insert(tempDto) == 1); //1개 삽입했는지 확인
        assertTrue(noticeDao.insert(tempDto) == 1); //1개 삽입했는지 확인
        assertTrue(noticeDao.count() == 2); //db데이터 2개인지 확인

        Long noticeId = noticeDao.selectAllAsc().get(0).getNotice_id(); //첫번째 noticeId 저장

        //3)업데이트용 dto 생성 - is_top_post=N
        sleep(2000); //2초 텀 주기
        String nowStr = LocalDateTime.now().toString(); //현재시간 생성
        NoticeDto updateDto = new NoticeDto(noticeId, "testing-updated", "testing content - updated", nowStr, "12",  "N", "Y");

        //4)update수행
        assertTrue(noticeDao.update(updateDto) == 1);
        assertTrue(noticeDao.count() == 2); //db에 그대로 data 2개인지

        //5)selectAllTopPost로 1개인지 확인 - is_top_post=y가 조건이므로
        assertTrue(noticeDao.selectAllTopPost().size()==1);
    }

    //9-2. content 내용 update하고 content 내용 같은지 판별
    @Test
    @Transactional
    public void update_content_equal_test_success() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2)is_post=y DTO 1개생성, DB에 1번 삽입
        NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
        assertTrue(noticeDao.insert(tempDto) == 1); //1개 삽입했는지 확인
        assertTrue(noticeDao.count() == 1); //db데이터 1개인지 확인

        Long noticeId = noticeDao.selectAllAsc().get(0).getNotice_id(); //첫번째 noticeId 저장

        //3)업데이트용 dto 생성 - is_top_post=N
        sleep(2000); //2초 텀 주기
        String nowStr = LocalDateTime.now().toString(); //현재시간 생성
        NoticeDto updateDto = new NoticeDto(noticeId, "testing-updated", "testing content - updated", nowStr, "12",  "N", "Y");

        //4)update수행
        assertTrue(noticeDao.update(updateDto) == 1);
        assertTrue(noticeDao.count() == 1); //db에 그대로 data 2개인지

        //5)notice_id로 검색해서 나온 dto의 내용과 updateDto의 내용이 같은지 확인
        assertTrue(noticeDao.selectById(noticeId).getNotice_content().equals(updateDto.getNotice_content()));
    }

    //9-3. update했을때 같은 객체인지 확인
    @Test
    @Transactional
    public void update_dto_equals_test_fail() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2)is_post=y DTO 1개생성, DB에 1번 삽입
        NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
        assertTrue(noticeDao.insert(tempDto) == 1); //1개 삽입했는지 확인
        assertTrue(noticeDao.count() == 1); //db데이터 1개인지 확인

        Long noticeId = noticeDao.selectAllAsc().get(0).getNotice_id(); //첫번째 noticeId 저장

        //3)업데이트용 dto 생성 - is_top_post=N
        sleep(2000); //2초 텀 주기
        String nowStr = LocalDateTime.now().toString(); //현재시간 생성
        NoticeDto updateDto = new NoticeDto(noticeId, "testing-updated", "testing content - updated", nowStr, "12",  "N", "Y");

        //4)update수행
        assertTrue(noticeDao.update(updateDto) == 1);
        assertTrue(noticeDao.count() == 1); //db에 그대로 data 2개인지

        //5)notice_id로 검색해서 나온 dto의 내용과 updateDto의 내용이 같은지 확인(writer, create_datetime이 없을것)
        assertFalse(noticeDao.selectById(noticeId).equals(updateDto));
        System.out.println("updateDto = " + updateDto);
        System.out.println("noticeDao.selectById(noticeId) = " + noticeDao.selectById(noticeId));
    }

    //9-4. not null인 컬럼에 에 null 주고 update해보기
    @Test
    @Transactional
    public void update_null_exception() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2) is_post=y DTO 1개생성, DB에 1번 삽입
        NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
        assertTrue(noticeDao.insert(tempDto) == 1); //1개 삽입했는지 확인
        assertTrue(noticeDao.insert(tempDto) == 1); //1개 삽입했는지 확인
        assertTrue(noticeDao.count() == 2); //db데이터 2개인지 확인


        //3) 첫번째 dto의 notice_id 저장
        Long noticeId = noticeDao.selectAllAsc().get(0).getNotice_id();

        //4)업데이트용 dto 생성 - title not null인데 null값을 준다.
        sleep(2000); //2초 텀 주기
        String nowStr = LocalDateTime.now().toString(); //현재시간 생성
        NoticeDto updateDto = new NoticeDto(noticeId, null, "testing content - updated", nowStr, "12",  "N", "Y");

        //5)update수행
        assertThrows(DataIntegrityViolationException.class, () -> noticeDao.update(updateDto));
    }

    //9-5. db에 없는 데이터 update해보기 - 0반환하고 끝 update X
    @Test
    @Transactional
    public void update_nonExist_success() throws Exception {
        //1)db 비우기
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2)is_post=y DTO 1개생성, DB에 1번 삽입
        NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "Y", "Y");
        assertTrue(noticeDao.insert(tempDto) == 1); //1개 삽입했는지 확인
        assertTrue(noticeDao.insert(tempDto) == 1); //1개 삽입했는지 확인
        assertTrue(noticeDao.count() == 2); //db데이터 2개인지 확인

        Long noticeId = noticeDao.selectAllAsc().get(0).getNotice_id(); //첫번째 noticeId 저장

        //3)업데이트용 dto 생성 - is_top_post=N
        sleep(2000); //2초 텀 주기
        String nowStr = LocalDateTime.now().toString(); //현재시간 생성
        NoticeDto updateDto = new NoticeDto(noticeId+2, "testing-updated", "testing content - updated", nowStr, "12",  "N", "Y");

        //4)update수행
        assertTrue(noticeDao.update(updateDto)==0);
    }

    //10. pagination - selectAllDescPage() 테스트
    //10-1. pageSize 확인
    @Test
    @Transactional
    public void selectAllDescPage_Test_success() throws Exception {
        //1) db비우고
        noticeDao.deleteAll();
        assertTrue(noticeDao.count() == 0);

        //2) 125개 넣는다
        int testNum = 125;
        for(int i = 1; i <= testNum; i++) {
            NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "N", "Y");
            assertTrue(noticeDao.insert(tempDto) == 1);
        }
        assertTrue(noticeDao.count() == testNum);

        //3) 총 게시글수 파악
        int totalCnt = noticeDao.count();
        assertTrue(totalCnt == testNum);

        //4) map 생성
        Map map = new HashMap();
        Integer page = 1;
        Integer pageSize = 10;
        map.put("offset", (page - 1) * pageSize);
        map.put("pageSize", pageSize);

        //첫페이지 가져왔을때 pageSize와 크기가 같은지 확인
        List resList = noticeDao.selectAllDescPage(map);
        assertTrue(resList.size() == pageSize);
    }

    //10-2. offset 0, pagesize = 3으로 설정시 map별로 id값이 같은지
    @Test
    @Transactional
    public void selectAllDescPage_offset_pageSize_Test_success() throws Exception {
        //1) db비우고
        noticeDao.deleteAll();

        //2)db에 데이터 10개 저장
        for (int i = 1; i <= 10; i++) {
            NoticeDto tempDto = new NoticeDto("testing", "testing content", "11", "N", "Y");
            noticeDao.insert(tempDto);
        }

        //3) offset, pageSize hashmap생성
        //0~2 - 10,9,8
        Map map = new HashMap();
        map.put("offset", 0);
        map.put("pageSize", 3);

        //4) map으로 pagination 적용한 리스트와 id값이 예상값과 같은지 확인
        List<NoticeDto> testList = noticeDao.selectAllDescPage(map);
        assertTrue(testList.get(0).getNotice_id() == 10);
        assertTrue(testList.get(1).getNotice_id() == 9);
        assertTrue(testList.get(2).getNotice_id() == 8);

        //0~0 - 10
        map = new HashMap();
        map.put("offset", 0);
        map.put("pageSize", 1);

        testList = noticeDao.selectAllDescPage(map);
        assertTrue(testList.get(0).getNotice_id() == 10);

        //7~9 - 3,2,1
        map = new HashMap();
        map.put("offset", 7);
        map.put("pageSize", 3);

        testList = noticeDao.selectAllDescPage(map);
        assertTrue(testList.get(0).getNotice_id() == 3);
        assertTrue(testList.get(1).getNotice_id() == 2);
        assertTrue(testList.get(2).getNotice_id() == 1);
    }
}

