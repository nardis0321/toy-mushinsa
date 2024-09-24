package com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dao;


import com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto.ProductDescriptionImg;
import com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto.ProductDescriptionImgDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDescriptionImgDao {

    int insert(List<ProductDescriptionImg> productDescriptionImgs) throws Exception;


    List<ProductDescriptionImgDetailDto> findAllByProductDescriptionId(@Param("productDescriptionId") String productDescriptionId) throws Exception;



    int deleteById(@Param("productDescriptionImgId") String productDescriptionImgId) throws Exception;
    int deleteByProductDesciprionId(String productDescriptionId) throws Exception;

//    void saveAll(@Param("ProductDescriptionImgList") List<ProductDescriptionImg> productDescriptionImgs);
}
