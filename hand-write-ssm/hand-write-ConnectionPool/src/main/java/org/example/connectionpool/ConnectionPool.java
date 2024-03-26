package org.example.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连接池的实现类
 */
public class ConnectionPool implements IConnectionPool {

    // 可以理解为计数器
    private AtomicInteger connCount = new AtomicInteger(0 );

    /**
     * 数据库的配置信息
     */
    private DataSourceConfig dataSourceConfig;

    /**
     * 声明一个线程安全的集合list集合,存储空闲的连接
     * ArrayList<Connection> list = new ArrayList<>(); 线程不安全
     * Vector<Connection> list = new Vector<>(); 线程安全
     */
    Vector<Connection> freePools = new Vector<Connection>();

    /**
     * 声明一个线程安全的集合list集合,存储正在使用的连接
     */
    Vector<Connection> usePools = new Vector<Connection>();

    /**
     * 构造方法 并初始化连接池
     * @param dataSourceConfig
     */
    public ConnectionPool(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
        //初始化连接池
        init();
    }

    /**
     * 连接池的初始化
     */
    public void init() {
        for (int i = 0; i < Integer.valueOf(dataSourceConfig.getInitSize()); i++) {
            Connection conn = createConn();
            System.out.println("初始化连接池，连接对象:"+conn);
            freePools.add(conn);
        }
    }


    /**
     * 创建连接池
     * @return
     */
    private synchronized Connection createConn() {
        Connection conn = null;
        try {
           // Class.forName(dataSourceConfig.getDriver()); 可省略
            conn = DriverManager.getConnection(
                    dataSourceConfig.getUrl(), dataSourceConfig.getUsername(), dataSourceConfig.getPassword()
            );
            //累加
            connCount.incrementAndGet();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    @Override
    public synchronized Connection getConn() {
        Connection conn = null;
        try {
            // 检查是否存在空闲连接池
            if (!freePools.isEmpty()){
                //从空闲连接集合中获取一个连接
                conn = freePools.get(0);
                if (isAvailable(conn)){
                    //从空闲连接集合中移除
                    freePools.remove(conn);
                    //添加到正在使用的连接集合
                    usePools.add(conn);
                }
                System.out.println("从空闲连接池中获取连接对象:"+conn+"，当前空闲连接池数量:"+freePools.size()+"，当前正在使用的连接池数量:"+usePools.size());
            }else {
                //判断连接池是否已满，如果没有则创建连接，如果已满则等待
                if (connCount.get() < Integer.valueOf(dataSourceConfig.getMaxSize())){
                    //创建连接
                    conn = createConn();
                    //添加到正在使用的连接集合
                    usePools.add(conn);
                    System.out.println("连接池没空闲连接，新建连接:"+conn+"，当前空闲连接池数量:"+freePools.size()+"，当前正在使用的连接池数量:"+usePools.size());
                }else {
                    //等待
                    this.wait(Integer.valueOf(dataSourceConfig.getWaittime()));
                    //重新调用方法
                    conn = getConn();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }


    /**
     * 判断连接是否可用
     * @param conn
     * @return boolean
     */
    private boolean isAvailable(Connection conn) {
        try {
            if (conn != null && conn.isClosed()){
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public void releaseConn(Connection conn) {

    }
}
