package org.example.test;

import org.example.connectionpool.ConnectionPool;
import org.example.connectionpool.DataSourceConfig;
import org.junit.Test;

public class TestDataSourceConfig {

    @Test
    public void test1() {
        DataSourceConfig instance = DataSourceConfig.getInstance();
        ConnectionPool connectionPool = new ConnectionPool(instance);
    }

}
