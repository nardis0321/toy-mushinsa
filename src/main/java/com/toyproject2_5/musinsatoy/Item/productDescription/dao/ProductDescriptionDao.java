package com.toyproject2_5.musinsatoy.Item.productDescription.dao;

import com.toyproject2_5.musinsatoy.Item.productDescription.dto.ProductDescription;
import com.toyproject2_5.musinsatoy.Item.productDescription.dto.ProductDescriptionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDescriptionDao {



    int insert(ProductDescription productDescription) throws Exception;


    ProductDescriptionDto findById(@Param("productDescriptionId") String productDescriptionId)throws Exception;
    ProductDescriptionDto findByProductId(@Param("productId") String productId) throws Exception;
    List<ProductDescriptionDto> findByCategoryId(@Param("categoryId") String categoryId) throws Exception;


    int update(ProductDescriptionDto productDescriptionDto) throws Exception;


    int delete(@Param("productDescriptionId") String productDescriptionId) throws Exception;

}
