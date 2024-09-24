package com.toyproject2_5.musinsatoy.salesorder;

import com.toyproject2_5.musinsatoy.salesorder.dao.SalesOrderProductOptionDao;
import com.toyproject2_5.musinsatoy.salesorder.dto.SalesOrderProductOptionDTOwithOrderIdAndProductId;
import com.toyproject2_5.musinsatoy.salesorder.entity.SalesOrderProductOption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SalesOrderProductOptionDaoTest {
    @Autowired
    SalesOrderProductOptionDao optionDao;

    @Test
    public void deleteAndCountTest(){
        optionDao.deleteAll();
        int cnt = optionDao.count();
        assertEquals(0, cnt);
    }

    @Test
    public void deleteAndInsertAndSelectTest(){
        optionDao.deleteAll();
        assertEquals(0, optionDao.count());

        SalesOrderProductOption inserted = new SalesOrderProductOption();
        inserted.setProductId("P002");
        inserted.setOptionId("A1");
        inserted.setOptionCategory("사이즈");
        inserted.setOptionName("X");
        inserted.setOptionDepth("1");

        optionDao.insert(inserted);

        SalesOrderProductOptionDTOwithOrderIdAndProductId orderIdAndProductId =
                new SalesOrderProductOptionDTOwithOrderIdAndProductId(inserted.getOrderId(), inserted.getProductId());
        List<SalesOrderProductOption> foundList = optionDao.findById(orderIdAndProductId);
        assertNotNull(foundList);
        assertFalse(foundList.isEmpty());

        SalesOrderProductOption found = foundList.get(0);

        assertEquals(inserted.getProductId(), found.getProductId());
        assertEquals(inserted.getOptionId(), found.getOptionId());
        assertEquals(inserted.getOptionCategory(), found.getOptionCategory());
        assertEquals(inserted.getOptionName(), found.getOptionName());
        assertEquals(inserted.getOptionDepth(), found.getOptionDepth());

    }
}
