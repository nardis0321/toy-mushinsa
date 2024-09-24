package com.toyproject2_5.musinsatoy.salesorder.dao;

import com.toyproject2_5.musinsatoy.salesorder.entity.SalesOrderProduct;

import java.util.List;

public interface SalesOrderProductDao {
    void insert(SalesOrderProduct product);
    List<SalesOrderProduct> findById(String orderId);
    void deleteAll();
    int count();
}
