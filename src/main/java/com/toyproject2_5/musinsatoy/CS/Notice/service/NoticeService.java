package com.toyproject2_5.musinsatoy.CS.Notice.service;

import com.toyproject2_5.musinsatoy.CS.Notice.dto.NoticeDto;

import java.util.List;
import java.util.Map;

public interface NoticeService {
    //1.저장 insert()
    int insert(NoticeDto noticeDto) throws Exception;

    //2. 공지 상세 -  selectById()
    NoticeDto selectById(Long id) throws Exception;

    //3. 공지 리스트 전체 내림차순 - selectAllDescPage()
    List<NoticeDto> selectAllDescPage(Map map) throws Exception;

    //4. 공지 상단 게시 리스트 내림차순 - selectAllTopPost()
    List<NoticeDto> selectAllTopPost() throws Exception;

    //4-1. 테스트용 공지 리스트 내림차순
    List<NoticeDto> selectAllAsc() throws Exception;

    //5. 수정 - update() - 제목, 내용,
    int update(NoticeDto noticeDto) throws Exception;

    //5. 삭제 - is_post를 "N"으로 변경하는 update()
    int delete(Long id, String modifier_id) throws Exception;

    //6. 공지 게시글 수 count
    int getCount() throws Exception;
}
