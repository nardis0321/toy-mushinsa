package com.toyproject2_5.musinsatoy.Item.product.dao.stock;

import com.toyproject2_5.musinsatoy.Item.product.dto.stock.StockDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.stock.StockRegisterDto;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class StockDaoMysql implements StockDao{

    private final SqlSession sqlSession;

    private String namespace ="stockMapper.";


    @Override
    public int insertStock(List<StockRegisterDto> stockRegisterDtoList) {
        return sqlSession.insert(namespace + "registerStock", stockRegisterDtoList);
    }

    @Override
    public List<StockDto> findByProductId(String productId) {
        return sqlSession.selectList(namespace + "findByProductId", productId);
    }
}
