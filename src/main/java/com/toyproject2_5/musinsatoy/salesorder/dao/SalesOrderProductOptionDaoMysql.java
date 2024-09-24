package com.toyproject2_5.musinsatoy.salesorder.dao;

import com.toyproject2_5.musinsatoy.salesorder.dto.SalesOrderProductOptionDTOwithOrderIdAndProductId;
import com.toyproject2_5.musinsatoy.salesorder.entity.SalesOrderProductOption;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SalesOrderProductOptionDaoMysql implements SalesOrderProductOptionDao{
    @Autowired
    private SqlSession session;
    private final String namespace = "com.toyproject2_5.musinsatoy.salesorder.dao.SalesOrderProductOptionMapper.";

    @Override
    public void insert(SalesOrderProductOption option) {
        session.insert(namespace+"insert", option);
    }

    @Override
    public List<SalesOrderProductOption> findById(
            SalesOrderProductOptionDTOwithOrderIdAndProductId orderIdAndProductId) {
        return session.selectList(namespace+"selectById", orderIdAndProductId);
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
