package com.toyproject2_5.musinsatoy.salesorder;

import com.toyproject2_5.musinsatoy.salesorder.dao.SalesOrderProductDao;
import com.toyproject2_5.musinsatoy.salesorder.entity.SalesOrderProduct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SalesOrderProductDaoTest {
    @Autowired
    SalesOrderProductDao productDao;

    @Test
    public void deleteAndCountTest(){
        productDao.deleteAll();
        int cnt = productDao.count();
        assertEquals(0, cnt);
    }

    @Test
    public void deleteAndInsertAndSelectTest(){
        productDao.deleteAll();
        SalesOrderProduct product = new SalesOrderProduct();
        product.setOrderId("240903165626771265");  // orderId
        product.setProductId("P002");              // productId
        product.setPrice(50000);                   // price
        product.setAmount(1);                      // amount
        product.setOptions(null);               // List<SalesOrderProductOption

        productDao.insert(product);

        List<SalesOrderProduct> foundProductList = productDao.findById(product.getOrderId());
        assertNotNull(foundProductList);  // 데이터가 있는지 확인
        SalesOrderProduct foundProduct = foundProductList.get(0);
        assertEquals(product.getProductId(), foundProduct.getProductId());
        assertEquals(product.getPrice(), foundProduct.getPrice());
    }
}
