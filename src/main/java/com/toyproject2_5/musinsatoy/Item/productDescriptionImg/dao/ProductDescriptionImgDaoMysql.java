package com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dao;


import com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto.ProductDescriptionImg;
import com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto.ProductDescriptionImgDetailDto;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;


@RequiredArgsConstructor
@Repository
public class ProductDescriptionImgDaoMysql implements ProductDescriptionImgDao {

    private final SqlSession sqlSession;
    private final String namespace = "productDescriptionImgMapper.";

    @Override
    public int insert(List<ProductDescriptionImg> productDescriptionImgs) throws Exception {
        return sqlSession.insert(namespace+"insert", productDescriptionImgs);
    }


    @Override
    public List<ProductDescriptionImgDetailDto> findAllByProductDescriptionId(String productDescriptionId) {
        return sqlSession.selectList(namespace+"findAllByProductDescriptionId", productDescriptionId);
    }


    @Override
    public int deleteByProductDesciprionId(String productDescriptionId) throws Exception {
        return sqlSession.update(namespace+"deleteAllByProductDesciprionId", productDescriptionId);
    }

    @Override
    public int deleteById(String productDescriptionImgId) throws Exception {
        return sqlSession.delete(namespace + "findById", productDescriptionImgId);
    }


}
