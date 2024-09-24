package com.toyproject2_5.musinsatoy.Item.product.dao;

import com.toyproject2_5.musinsatoy.Item.product.dto.option.OptionDBregisterDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.option.OptionStockDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OptionDao {

    int insertOption(List<OptionDBregisterDto> optionDBregisterDtoList);

    List<OptionStockDetailDto> findDetailOptionStockByProductId(@Param("productId") String productId);
}
