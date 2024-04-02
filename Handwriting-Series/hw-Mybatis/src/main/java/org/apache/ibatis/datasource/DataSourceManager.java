package org.apache.ibatis.datasource;

import java.sql.Connection;

/**
 * 数据源管理器
 */
public class DataSourceManager {

    /**
     * 静态变量初始化
     */
    static DataSourceConfig instance = DataSourceConfig.getInstance();
    static ConnectionPool connectionPool = new ConnectionPool(instance);



    /**
     * 从连接池中获取一个连接对象
     */
    public static Connection getConn() {
        return connectionPool.getConn();
    }

    /**
     * 归还连接对象到连接池中
     * @param conn
     */
    public static void releaseConn(Connection conn) {
        connectionPool.releaseConn(conn);
    }
}
