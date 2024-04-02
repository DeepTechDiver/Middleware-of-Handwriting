package org.example.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
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
    Vector<ConnectionEntity> usePools = new Vector<ConnectionEntity>();

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
        // 开启健康检查，防止连接超时不释放
        if (Boolean.valueOf(dataSourceConfig.getHealth()) == true){
            checkConnTimeOut();
        }
    }

    /**
     * 定时检查
     * 定时任务框架 quartz、Spring Task、xxlJob
     */
    private void checkConnTimeOut() {
        Worker worker = new Worker();
        //调度任务,延迟执行
        new Timer().schedule(worker, Long.valueOf(dataSourceConfig.getDelay()), Long.valueOf(dataSourceConfig.getPeriod()));
    }

    class Worker extends TimerTask{
        @Override
        public void run() {
            System.out.println("开始检查连接超时");
            try {
                //遍历正在使用的线程池
                for (int i = 0; i < usePools.size(); i++) {
                    ConnectionEntity connectionEntity = usePools.get(i);
                    Long userStartTime = connectionEntity.getUserStartTime();
                    //超时检查逻辑:  当前时间  -  创建连接时间  > 超时时间 （连接超时）
                    if (System.currentTimeMillis() - userStartTime > Long.valueOf(dataSourceConfig.getTimeout())){
                        Connection connection = connectionEntity.getConnection();
                        if (isAvailable(connection)){
                            connection.close();
                            //从正在使用的连接集合中移除
                            //TODO 不合理
                            usePools.remove(i);
                            //连接总数-1 保证原子一致性
                            connCount.decrementAndGet();
                            System.out.println(Thread.currentThread().getName()+"定期检查连接状态: "+connection+ "进行废弃'，当前空闲连接池数量:"+freePools.size()+"，当前正在使用的连接池数量:"+usePools.size());

                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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
                    //记录创建连接数据库的时间戳
                    ConnectionEntity connEntity = new ConnectionEntity(conn, System.currentTimeMillis());
                    //添加到正在使用的连接集合
                    usePools.add(connEntity);
                }
                System.out.println(Thread.currentThread().getName()+"从空闲连接池中获取连接对象:"+conn+"，当前空闲连接池数量:"+freePools.size()+"，当前正在使用的连接池数量:"+usePools.size());
            }else {
                //判断连接池是否已满，如果没有则创建连接，如果已满则等待
                if (connCount.get() < Integer.valueOf(dataSourceConfig.getMaxSize())){
                    //创建连接
                    conn = createConn();
                    //添加到正在使用的连接集合
                    ConnectionEntity connEntity = new ConnectionEntity(conn, System.currentTimeMillis());
                    usePools.add(connEntity);
                    System.out.println(Thread.currentThread().getName()+"连接池没空闲连接，新建连接:"+conn+"，当前空闲连接池数量:"+freePools.size()+"，当前正在使用的连接池数量:"+usePools.size());
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
            if (conn != null && !conn.isClosed()){
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * 释放连接
     * @param conn
     */
    @Override
    public synchronized void releaseConn(Connection conn) {
        if (isAvailable(conn)){
            freePools.add(conn);
            for (int i = 0; i < usePools.size(); i++){
                // Connection connection = usePools.get(i).getConnection();
                if (usePools.get(i).getConnection().equals(conn)){
                    usePools.remove(i);
                }
            }
            System.out.println(Thread.currentThread().getName()+"归还连接对象:"+conn+"，当前空闲连接池数量:"+freePools.size()+"，当前正在使用的连接池数量:"+usePools.size());
        }
        this.notify();
    }
}
