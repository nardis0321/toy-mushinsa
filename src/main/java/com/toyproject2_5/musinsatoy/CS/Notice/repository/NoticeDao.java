package com.toyproject2_5.musinsatoy.CS.Notice.repository;

import com.toyproject2_5.musinsatoy.CS.Notice.dto.NoticeDto;

import java.util.List;
import java.util.Map;

public interface NoticeDao {
    //1. count
    int count() throws Exception;

    //2. insert DTO
    int insert(NoticeDto noticeDto) throws Exception;

    //3. select - 단일 DTO
    NoticeDto selectById(Long id) throws Exception;

    //4. selectAllDesc - DTO list
    List<NoticeDto> selectAllDesc() throws Exception;

    //4-1. selectAllDescPage - Dto list
    List<NoticeDto> selectAllDescPage(Map map) throws Exception;

    //4-2. selectAllAsc - DTO list
    List<NoticeDto> selectAllAsc() throws Exception;

    //4-3. selectAllTopPost - DTO list
    List<NoticeDto> selectAllTopPost() throws Exception;

    //5. update - update용 DTO 받아서 update
    int update(NoticeDto noticeDto) throws Exception;

    //6. delete - id 받아서 해당 DTO 삭제
    int deleteById(Long id) throws Exception;

    //7. 전체 삭제
    int deleteAll() throws Exception;
}
