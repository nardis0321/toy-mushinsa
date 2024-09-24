package com.toyproject2_5.musinsatoy.Item.brand.dao;

import com.toyproject2_5.musinsatoy.Item.brand.dto.BrandListDto;
import com.toyproject2_5.musinsatoy.Item.brand.dto.BrandCreateDto;
import com.toyproject2_5.musinsatoy.Item.brand.dto.BrandDto;
import com.toyproject2_5.musinsatoy.Item.brand.dto.BrandUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface BrandDao {

    int insert(BrandCreateDto brand) throws Exception;

    BrandDto findById(@Param("brandId") String brandId);
    List<BrandListDto> brandListPaging(HashMap<String, Object> map) throws Exception;
    int countAllBrand();



    int update(BrandUpdateDto updateDto) throws  Exception;



    int delete(String brandId) throws Exception;








}
