package com.toyproject2_5.musinsatoy.CS.FAQ.service;

import com.toyproject2_5.musinsatoy.CS.FAQ.dto.FaqDto;
import com.toyproject2_5.musinsatoy.ETC.FaqSearchCondition;

import java.util.List;
import java.util.Map;

public interface FaqService {
    //1. 갯수 - count()
    int count() throws Exception;

    //2. 저장 - insert()
    int insert(FaqDto faqDto) throws Exception;

    //3. FAQ 상세 - findById()
    FaqDto selectById(Integer id) throws Exception;

    //4. 리스팅
    //4-1. FAQ 리스트 오름차순(테스트용) - selectAllAsc()
    List<FaqDto> selectAllAsc() throws Exception;

    //4-2. FAQ 리스트 내림차순 - selectAllDesc()
    List<FaqDto> selectAllCatDesc() throws Exception;

    //4-3. FAQ 리스트 내림차순 category_code 필터링 - selectAllCatCodeDesc()
    List<FaqDto> selectAllCatCodeDesc(String faq_category_code) throws Exception;

    //4-4. FAQ 리스트 내림차순 category_code, subcategory_id 필터링 - selectAllCatCodeSubIdDesc()
    List<FaqDto> selectAllCatCodeSubIdDesc(Map map) throws Exception;

    //5. 수정 - update(), 제목, 내용, 카테고리코드, 서브카테고리id
    int update(FaqDto faqDto) throws Exception;

    //6. 삭제 - delete(), is_post를 "N"으로 변경
    int delete(Integer id, String modifier_id) throws Exception;

    //7. 검색
    //7-1. 키워드로 제목, 내용에서 검색 - searchSelect()
    List<FaqDto> searchSelect(FaqSearchCondition sc) throws Exception;

    //7-2. 키워드로 제목, 내용에서 검색된 데이터의 갯수 - searchResultCnt()
    int searchResultCnt(FaqSearchCondition sc) throws Exception;
}
