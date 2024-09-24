package com.toyproject2_5.musinsatoy.CS.Notice.repository;


import com.toyproject2_5.musinsatoy.CS.Notice.dto.NoticeDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor //@Autowired를 사용하지 않고 의존성 주입
public class NoticeDaoImpl implements NoticeDao {
    private final SqlSessionTemplate sql; //매퍼 호출, dao 공유가능, thread-safe

    private static final String NAMESPACE = "com.toyproject2_5.musinsatoy.cs.notice.NoticeMapper.";

    //1. count
    @Override
    public int count() throws Exception {
        return sql.selectOne(NAMESPACE+"count"); //mapper의 NAMESPACE.NAMESPACE id 인 태그 호출
    }

    //2. insert DTO
    @Override
    public int insert(NoticeDto noticeDto) throws Exception {
        return sql.insert(NAMESPACE+"insert", noticeDto);
    }

    //3. select - 단일 DTO
    @Override
    public NoticeDto selectById(Long id) throws Exception {
        return sql.selectOne(NAMESPACE+"selectById", id);
    }

    //4. selectAllDesc - DTO list, is_post="Y"인 것만 내림차순
    @Override
    public List<NoticeDto> selectAllDesc() throws Exception {
        return sql.selectList(NAMESPACE+"selectAllDesc");
    }

    //4-1. selectAllDescPage = DTO list, is_post="Y" 내림차순, pagination
    @Override
    public List<NoticeDto> selectAllDescPage(Map map) throws Exception {
        return sql.selectList(NAMESPACE+"selectAllDescPage", map);
    }

    //4-3. selectAllAsc - DTO list,  is_post="Y"인 것만 오름차순
    @Override
    public List<NoticeDto> selectAllAsc() throws Exception {
        return sql.selectList(NAMESPACE+"selectAllAsc");
    }

    //4-2. selectAllTopPost - DTO list, is_top_post="Y", is_post="Y"인 것만 찾아서 listing
    @Override
    public List<NoticeDto> selectAllTopPost() throws Exception {
        return sql.selectList(NAMESPACE+"selectAllTopPost");
    }

    //5. update - DTO 받아서 update
    @Override
    public int update(NoticeDto noticeDto) throws Exception {
        return sql.update(NAMESPACE+"update", noticeDto);
    }

    //6. delete - id 받아서 해당 DTO 삭제
    @Override
    public int deleteById(Long id) throws Exception {
        return sql.delete(NAMESPACE+"deleteById", id);
    }

    //7. 전체 삭제
    @Override
    public int deleteAll() throws Exception{
        return sql.delete(NAMESPACE+"deleteAll");
    }
}
