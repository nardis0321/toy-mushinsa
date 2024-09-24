package com.toyproject2_5.musinsatoy.Item.product.dao.stock;

import com.toyproject2_5.musinsatoy.Item.product.dao.OptionDao;
import com.toyproject2_5.musinsatoy.Item.product.dto.option.OptionDBregisterDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.stock.StockDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.stock.StockRegisterDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class StockDaoMysqlTest {

    @Autowired
    private StockDaoMysql stockDao;

    @Qualifier("optionDaoMysql")
    @Autowired
    private OptionDao optionDao;


    @Test
    public void stockRegisterTest(){
        List<StockRegisterDto> stockRegisterDtos = new ArrayList<>();

        String productId = "P001";

        String[] optionRegister = {"A1B1C1","A1B1C2","A1B2C1","A1B2C2","A2B1C1","A2B1C2","A2B2C1","A2B2C2"};


        String[] optionName = {"RED", "BLUE","RED", "BLUE","RED", "BLUE","RED", "BLUE"};
        List<OptionDBregisterDto>  dBregisterDtos= new ArrayList<>();
        for (int i =0;i<optionRegister.length;i++){
            dBregisterDtos.add(new OptionDBregisterDto(productId, optionRegister[i] ,optionName[i],3,"컬러" ));
        }
        optionDao.insertOption(dBregisterDtos);


        for(int i = 0; i < optionRegister.length; i++){
            stockRegisterDtos.add(new StockRegisterDto(productId,null,optionRegister[i],i));
        }

        stockDao.insertStock(stockRegisterDtos);

        List<StockDto>  stocks = stockDao.findByProductId(productId);

        stocks.forEach(System.out::println);
    }



}