package com.toyproject2_5.musinsatoy.Item.product.dao;

import com.toyproject2_5.musinsatoy.Item.product.dto.option.OptionDBregisterDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.option.OptionStockDetailDto;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class OptionDaoMysql implements OptionDao {

    private final SqlSession sqlSession;

    private String namespace ="optionMapper.";


    @Override
    public int insertOption(List<OptionDBregisterDto> optionDBregisterDtoList) {
        return sqlSession.insert(namespace + "registerOption", optionDBregisterDtoList);
    }

    @Override
    public List<OptionStockDetailDto> findDetailOptionStockByProductId(String productId) {
        return sqlSession.selectList(namespace + "findDetailOptionStockByProductId", productId);
    }


}
