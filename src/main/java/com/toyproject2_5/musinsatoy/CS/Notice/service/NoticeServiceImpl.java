package com.toyproject2_5.musinsatoy.CS.Notice.service;

import com.toyproject2_5.musinsatoy.CS.Notice.dto.NoticeDto;
import com.toyproject2_5.musinsatoy.CS.Notice.repository.NoticeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

//트랜잭션 처리
//2. 사진 기능 추가시 (1개의 tx에서 먼저 공지data생성하고 공지사진 data생성)
//1) 삽입
//공지 DTO로 DB 먼저 생성하고 삽입
//notice_id값을 받아와서 공지사진 DTO 에 set
//공지 사진 DTO로 DB에 데이터 삽입
//2) 삭제

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final NoticeDao noticeDao;

    //1.저장 insert()
    @Override
    public int insert(NoticeDto noticeDto) throws Exception{

        return noticeDao.insert(noticeDto);
    }

    //2. 공지 상세 -  selectById()
    @Override
    public NoticeDto selectById(Long id) throws Exception {
        return noticeDao.selectById(id);
    }

    //3. 공지 리스트 전체 내림차순 페이지네이션 - selectAllDescPage()
    @Override
    public List<NoticeDto> selectAllDescPage(Map map) throws Exception{
        return noticeDao.selectAllDescPage(map);
    }

    //4. 공지 상단 게시 리스트 내림차순 - selectAllTopPost()
    @Override
    public List<NoticeDto> selectAllTopPost() throws Exception{
        return noticeDao.selectAllTopPost();
    }

    //4-1. 테스트용 공지 리스트 오름차순
    @Override
    public List<NoticeDto> selectAllAsc() throws Exception{
        return noticeDao.selectAllAsc();
    }

    //5. 수정 - update() - 제목, 내용,
    @Override
    public int update(NoticeDto noticeDto) throws Exception{
        //수정시간 생성
        String nowStr = LocalDateTime.now().toString();
        //수정할 updateDto 생성
        NoticeDto updateDto = new NoticeDto(noticeDto.getNotice_id(), noticeDto.getNotice_title(), noticeDto.getNotice_content(), nowStr, noticeDto.getModifier(), noticeDto.getIs_top_post(), noticeDto.getIs_post());
        return noticeDao.update(updateDto);
    }

    //5. 삭제 - is_post를 "N"으로 변경하는 update()
    @Override
    public int delete(Long id, String modifier_id) throws Exception {
        //해당 id로 글 찾고
        NoticeDto selectDto = noticeDao.selectById(id);
        //현재시간 생성
        String nowStr = LocalDateTime.now().toString();
        //updateDto 생성
        NoticeDto updateDto = new NoticeDto(selectDto.getNotice_id(), selectDto.getNotice_title(), selectDto.getNotice_content(), nowStr, modifier_id, selectDto.getIs_top_post(), "N");
        return noticeDao.update(updateDto);
    }

    //6. 공지 게시글 수 count
    @Override
    public int getCount() throws Exception {
        return noticeDao.count();
    }

}
