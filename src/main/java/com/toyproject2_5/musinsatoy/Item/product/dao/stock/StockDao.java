package com.toyproject2_5.musinsatoy.Item.product.dao.stock;

import com.toyproject2_5.musinsatoy.Item.product.dto.stock.StockDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.stock.StockRegisterDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StockDao {

    int insertStock(List<StockRegisterDto> stockRegisterDtoList);
    List<StockDto> findByProductId (@Param("productId") String productId);
}
