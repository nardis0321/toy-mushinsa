package com.toyproject2_5.musinsatoy.salesorder.dao;

import com.toyproject2_5.musinsatoy.salesorder.entity.SalesOrderProduct;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SalesOrderProductDaoMysql implements SalesOrderProductDao{
    @Autowired
    private SqlSession session;
    private final String namespace = "com.toyproject2_5.musinsatoy.salesorder.dao.SalesOrderProductMapper.";

    @Override
    public void insert(SalesOrderProduct product) {
        session.insert(namespace+"insert", product);
    }

    @Override
    public List<SalesOrderProduct> findById(String orderId) {
        return session.selectList(namespace+"selectById", orderId);
    }

    @Override
    public void deleteAll() {
        session.delete(namespace+"deleteAll");
    }

    @Override
    public int count() {
        return session.selectOne(namespace+"count");
    }
}
