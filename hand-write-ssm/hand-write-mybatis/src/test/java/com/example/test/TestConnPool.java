package com.example.test;

import org.example.connectionpool.DataSourceConfig;
import org.example.connectionpool.DataSourceManager;
import org.junit.Test;

import java.sql.Connection;

public class TestConnPool {


    @Test
    public void testConnPool() {
        DataSourceConfig.getInstance().setDriver("com.mysql.cj.jdbc.Driver");
        DataSourceConfig.getInstance().setUrl("jdbc:mysql://localhost:3306/basic");
        DataSourceConfig.getInstance().setUsername("root");
        DataSourceConfig.getInstance().setPassword("123456");

        Connection conn = DataSourceManager.getConn();
        System.out.println("获取连接成功");
        DataSourceManager.releaseConn(conn);

    }
}
