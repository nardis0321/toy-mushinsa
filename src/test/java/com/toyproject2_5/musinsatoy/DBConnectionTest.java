package com.toyproject2_5.musinsatoy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DBConnectionTest {
    @Autowired
    DataSource ds;

    @Test
    public void jdbcConnectionTest() throws Exception{
        Connection conn = ds.getConnection();

        System.out.println("conn = " + conn);
        assertNotNull(conn);
    }
}
