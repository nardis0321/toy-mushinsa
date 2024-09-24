package com.toyproject2_5.musinsatoy.salesorder.dao;

import com.toyproject2_5.musinsatoy.salesorder.dto.SalesOrderProductOptionDTOwithOrderIdAndProductId;
import com.toyproject2_5.musinsatoy.salesorder.entity.SalesOrderProductOption;

import java.util.List;

public interface SalesOrderProductOptionDao {
    void insert(SalesOrderProductOption option);
    List<SalesOrderProductOption> findById(
            SalesOrderProductOptionDTOwithOrderIdAndProductId orderIdAndProductId);
    void deleteAll();
    int count();
}
