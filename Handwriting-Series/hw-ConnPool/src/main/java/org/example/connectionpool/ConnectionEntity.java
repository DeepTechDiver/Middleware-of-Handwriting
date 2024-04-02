package org.example.connectionpool;

import java.sql.Connection;

public class ConnectionEntity {

    /**
     * 连接对象
     */
    private  Connection connection;

    /**
     * 开始使用的时间（时间戳）
     */
    private Long userStartTime;

    public ConnectionEntity(Connection connection, Long userStartTime) {
        this.connection = connection;
        this.userStartTime = userStartTime;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Long getUserStartTime() {
        return userStartTime;
    }

    public void setUserStartTime(Long userStartTime) {
        this.userStartTime = userStartTime;
    }
}
