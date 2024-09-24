package com.toyproject2_5.musinsatoy.CS.FAQ.repository;

import com.toyproject2_5.musinsatoy.CS.FAQ.dto.FaqDto;
import com.toyproject2_5.musinsatoy.ETC.FaqSearchCondition;

import java.util.List;
import java.util.Map;

public interface FaqDao {
    //1. count
    int count() throws Exception;

    //2. insertDto
    int insert(FaqDto faqDto) throws Exception;

    //3. selectById
    FaqDto selectById(Integer id) throws Exception;

    //4. selectAllDesc
    List<FaqDto> selectAllDesc() throws Exception;

    //4-1. selectAllAsc(테스트용)
    List<FaqDto> selectAllAsc() throws Exception;

    //4-2. SelectAllDesd Cat Code 추가(메인 리스트 사용)
    List<FaqDto> selectAllCatDesc() throws Exception;

    //5. selectAllCatCodeDesc
    List<FaqDto> selectAllCatCodeDesc(String faq_category_code) throws Exception;

    //6. selectAllCatCodeSubIdDesc
    List<FaqDto> selectAllCatCodeSubIdDesc(Map map) throws Exception;

    //7. update
    int update(FaqDto faqDto) throws Exception;

//    8. deleteById
    int deleteById(Integer id) throws Exception;

    //9. deleteAll
    int deleteAll() throws Exception;

    //10. searchSelect
    List<FaqDto> searchSelect(FaqSearchCondition sc) throws Exception;

    //11. searchResultCnt
    int searchResultCnt(FaqSearchCondition sc) throws Exception;
}
