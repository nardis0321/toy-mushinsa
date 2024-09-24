package com.toyproject2_5.musinsatoy.CS.FAQ.repository;

import com.toyproject2_5.musinsatoy.CS.FAQ.dto.FaqDto;
import com.toyproject2_5.musinsatoy.ETC.FaqSearchCondition;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class FaqDaoImpl implements FaqDao {
    private final SqlSessionTemplate sql;

    private static String NAMESPACE = "com.toyproject2_5.musinsatoy.cs.notice.FaqMapper.";

    //1. count
    @Override
    public int count() throws Exception {
        return sql.selectOne(NAMESPACE+"count");
    }

    //2. insertDto
    @Override
    public int insert(FaqDto faqDto) throws Exception {
        return sql.insert(NAMESPACE+"insert", faqDto);
    }

    //3. selectById
    @Override
    public FaqDto selectById(Integer id) throws Exception {
        return sql.selectOne(NAMESPACE+"selectById", id);
    }

    //4. selectAllDesc(테스트용)
    @Override
    public List<FaqDto> selectAllDesc() throws Exception {
        return sql.selectList(NAMESPACE+"selectAllDesc");
    }

    //4-1. selectAllCatDesc catCode 추가(메인 리스트)
    @Override
    public List<FaqDto> selectAllCatDesc() throws Exception {
        return sql.selectList(NAMESPACE+"selectAllCatDesc");
    }

    //4-1. selectAllAsc(API)
    @Override
    public List<FaqDto> selectAllAsc() throws Exception {
        return sql.selectList(NAMESPACE+"selectAllAsc");
    }

    //5. selectAllCatCodeDesc
    @Override
    public List<FaqDto> selectAllCatCodeDesc(String faq_category_code) throws Exception {
        return sql.selectList(NAMESPACE+"selectAllCatCodeDesc", faq_category_code);
    }

    //6. selectAllCatCodeSubIdDesc
    @Override
    public List<FaqDto> selectAllCatCodeSubIdDesc(Map map) throws Exception {
        return sql.selectList(NAMESPACE+"selectAllCatCodeSubIdDesc", map);
    }

    //7. update
    @Override
    public int update(FaqDto faqDto) throws Exception {
        return sql.update(NAMESPACE+"update", faqDto);
    }

    //8. deleteById
    @Override
    public int deleteById(Integer id) throws Exception {
        return sql.delete(NAMESPACE+"deleteById", id);
    }

    //9. deleteAll
    @Override
    public int deleteAll() throws Exception{
        return sql.delete(NAMESPACE+"deleteAll");
    }

    //10. searchSelect
    @Override
    public List<FaqDto> searchSelect(FaqSearchCondition sc) throws Exception {
        return sql.selectList(NAMESPACE+"searchSelect", sc);
    }

    //11. searchResultCnt
    @Override
    public int searchResultCnt(FaqSearchCondition sc) throws Exception {
        return sql.selectOne(NAMESPACE+"searchResultCnt", sc);
    }
}
