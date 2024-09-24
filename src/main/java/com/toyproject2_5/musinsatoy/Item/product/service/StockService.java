package com.toyproject2_5.musinsatoy.Item.product.service;

import com.toyproject2_5.musinsatoy.Item.product.dao.stock.StockDao;
import com.toyproject2_5.musinsatoy.Item.product.dto.stock.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StockService {

    private final StockDao stockDao;


    public List<StockDto> findByProductId(String productId){
        return stockDao.findByProductId(productId);
    }

}
