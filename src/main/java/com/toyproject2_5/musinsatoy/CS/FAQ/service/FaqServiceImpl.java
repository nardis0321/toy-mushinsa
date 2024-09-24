package com.toyproject2_5.musinsatoy.CS.FAQ.service;

import com.toyproject2_5.musinsatoy.CS.FAQ.dto.FaqDto;
import com.toyproject2_5.musinsatoy.CS.FAQ.repository.FaqDao;
import com.toyproject2_5.musinsatoy.ETC.FaqSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {
    private final FaqDao faqDao;

    //1. 갯수 - count()
    @Override
    public int count() throws Exception {
        return faqDao.count();
    }

    //2. 저장 - insert()
    @Override
    public int insert(FaqDto faqDto) throws Exception {
        return faqDao.insert(faqDto);
    }

    //3. FAQ 상세 - findById()
    @Override
    public FaqDto selectById(Integer id) throws Exception {
        return faqDao.selectById(id);
    }

    //4. 리스팅
    //4-1. FAQ 리스트 오름차순(API) - selectAllAsc()
    @Override
    public List<FaqDto> selectAllAsc() throws Exception {
        return faqDao.selectAllAsc();
    }

    //4-2. FAQ 리스트 내림차순 - selectAllDesc()
    @Override
    public List<FaqDto> selectAllCatDesc() throws Exception {
        return faqDao.selectAllCatDesc();
    }

    //4-3. FAQ 리스트 내림차순 category_code 필터링 - selectAllCatCodeDesc()
    @Override
    public List<FaqDto> selectAllCatCodeDesc(String faq_category_code) throws Exception {
        return faqDao.selectAllCatCodeDesc(faq_category_code);
    }

    //4-4. FAQ 리스트 내림차순 category_code, subcategory_id 필터링 - selectAllCatCodeSubIdDesc()
    @Override
    public List<FaqDto> selectAllCatCodeSubIdDesc(Map map) throws Exception {
        return faqDao.selectAllCatCodeSubIdDesc(map);
    }

    //5. 수정 - update(), 제목, 내용, 카테고리코드, 서브카테고리id
    @Override
    public int update(FaqDto faqDto) throws Exception {
        String nowStr = LocalDateTime.now().toString();

        FaqDto updateDto = new FaqDto(faqDto.getFaq_id(), faqDto.getFaq_category_code(), faqDto.getFaq_subcategory_id(), faqDto.getFaq_title(), faqDto.getFaq_content(), nowStr, faqDto.getModifier(), faqDto.getIs_post());
        return faqDao.update(updateDto);
    }

    //6. 삭제 - delete(), is_post를 "N"으로 변경
    @Override
    public int delete(Integer id, String modifier_id) throws Exception {
        //1)해당 id로 게시물 찾기
        FaqDto selectDto = faqDao.selectById(id);
        if(selectDto == null) throw new Exception("Service delete select failed");
        //2)updateDto 생성
        String nowStr = LocalDateTime.now().toString();
        FaqDto updateDto = new FaqDto(selectDto.getFaq_id(), selectDto.getFaq_category_code(), selectDto.getFaq_subcategory_id(), selectDto.getFaq_title(), selectDto.getFaq_content(), nowStr, modifier_id, "N");
        //3)update 수행
        return faqDao.update(updateDto);
    }

    //7. 검색
    //7-1. 키워드로 제목, 내용에서 검색 - searchSelect()
    @Override
    public List<FaqDto> searchSelect(FaqSearchCondition sc) throws Exception {
        return faqDao.searchSelect(sc);
    }

    //7-2. 키워드로 제목, 내용에서 검색된 데이터의 갯수 - searchResultCnt()
    @Override
    public int searchResultCnt(FaqSearchCondition sc) throws Exception {
        return faqDao.searchResultCnt(sc);
    }
}
