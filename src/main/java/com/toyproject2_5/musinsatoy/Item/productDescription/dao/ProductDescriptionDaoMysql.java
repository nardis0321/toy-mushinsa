package com.toyproject2_5.musinsatoy.Item.productDescription.dao;


import com.toyproject2_5.musinsatoy.Item.productDescription.dto.ProductDescription;
import com.toyproject2_5.musinsatoy.Item.productDescription.dto.ProductDescriptionDto;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;


@RequiredArgsConstructor
@Repository
public class ProductDescriptionDaoMysql implements ProductDescriptionDao {

    private final SqlSession sqlSession;

    private final String namespace = "productDescriptionMapper.";

    @Override
    public ProductDescriptionDto findById(String productDescriptionId) throws Exception{
        return sqlSession.selectOne(namespace+"findById", productDescriptionId);
    }

    @Override
    public int insert(ProductDescription productDescription) throws Exception {
        return sqlSession.insert(namespace+"insert", productDescription);

    }

    @Override
    public int delete(String productDescriptionId) throws Exception {
        return sqlSession.delete(namespace+"delete", productDescriptionId);
    }

    @Override
    public ProductDescriptionDto findByProductId(String productId) throws Exception {
        return sqlSession.selectOne(namespace+"findByProductId", productId);
    }

    @Override
    public List<ProductDescriptionDto> findByCategoryId(String categoryId) throws Exception {
        return sqlSession.selectList(namespace + "findByCategoryId", categoryId);
    }

    @Override
    public int update(ProductDescriptionDto productDescriptionDto) throws Exception {
        return sqlSession.update(namespace+"updateDescription", productDescriptionDto);
    }


}
