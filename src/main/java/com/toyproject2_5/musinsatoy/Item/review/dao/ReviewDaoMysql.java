package com.toyproject2_5.musinsatoy.Item.review.dao;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ReviewDaoMysql implements ReviewDao {

    private final SqlSession sqlSession;

    private String namespace = "ReviewMapper.";



}
