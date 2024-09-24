package com.toyproject2_5.musinsatoy.Item.brand.dao;

import com.toyproject2_5.musinsatoy.Item.brand.dto.BrandCreateDto;
import com.toyproject2_5.musinsatoy.Item.brand.dto.BrandDto;
import com.toyproject2_5.musinsatoy.Item.brand.dto.BrandListDto;
import com.toyproject2_5.musinsatoy.Item.brand.dto.BrandUpdateDto;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class BrandDaoMysql implements BrandDao{

    private final SqlSession sqlSession;

    private String namespace ="brandMapper.";

    @Override
    public int insert(BrandCreateDto brand) throws Exception {
        return  sqlSession.insert(namespace+"insertBrand", brand);
    }

    @Override
    public BrandDto findById(String brandId) {
        return sqlSession.selectOne(namespace+"findById", brandId);
    }



    @Override
    public List<BrandListDto> brandListPaging(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList(namespace+"brandListPaging", map);
    }

    @Override
    public int countAllBrand() {
        return sqlSession.selectOne(namespace+"countAllBrand");

    }

    @Override
    public int update(BrandUpdateDto updateDto) throws Exception {
        return sqlSession.update(namespace+"updateBrand",updateDto);
    }

    @Override
    public int delete(String brandId) throws Exception {
        return sqlSession.delete(namespace+"deleteById", brandId);
    }

}
