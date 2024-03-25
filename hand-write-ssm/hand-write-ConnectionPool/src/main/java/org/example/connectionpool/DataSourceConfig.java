package org.example.connectionpool;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

/**
 * 读取数据库配置文件，封装一个配置类
 */
public class DataSourceConfig {

    private String driver;
    private String url;
    private String username;
    private String password;
    private String initSize = "3";
    private String maxSize = "6";
    private String health = "true";
    private String delay = "2000";
    private String period = "2000";
    private String timeout = "100000";


    /**
     * 当一个对象被初始化时，可能会经历以下步骤：
     * 1.分配内存空间。
     * 2.初始化对象。
     * 3.将对象指向分配的内存空间。
     * 在某些情况下，编译器或处理器可能会对这些步骤进行重排序，例如先分配内存空间然后再初始化对象。这种重排序可能导致一个线程在检查 instance 变量不为 null 时，获取到一个未完全初始化的对象，从而导致程序出现异常。通过将 instance 变量声明为 volatile，可以禁止这种重排序，从而确保在对象完全初始化之前，不会返回不完整的对象。
     * volatile 关键字在这里用于保证双重检查锁定的线程安全性和正确性。
     */
//    volatile private static DataSourceConfig instance = null;
    private static DataSourceConfig instance = null;

    /**
     * 双重检查懒汉式单例设计模式
     */
    public static DataSourceConfig getInstance() {
        //使用双重检查锁定技术 --- 双重检查锁定是懒汉式单例模式的一种线程安全实现方式，通过在必要时才进行同步，避免了每次调用 getInstance() 方法都进行同步的性能开销。
        //第一次检查：最初，方法检查实例变量是否为 null。如果不为 null，则意味着已经创建了一个实例，因此它只需返回该实例。
        if (instance == null) {
            //同步块：如果实例变量为 null，则进入一个同步块，使用类对象（DataSourceConfig.class）作为监视器。这确保只有一个线程可以同时进入该块。
            // 静态的锁是class 普通的锁是this
            synchronized (DataSourceConfig.class) {
                //第二次检查：在同步块内部，再次检查实例变量是否为 null。以防止多个线程同时到达同步块并创建多个 DataSourceConfig 实例。如果在同步块中实例仍然为 null，则创建一个新的 DataSourceConfig 实例。
                if (instance == null) {
                    instance = new DataSourceConfig();
                }
            }
        }
        //返回实例：最后，返回实例变量。
        return instance;
    }

    /**
     * 私有化构造方法
     */
    private DataSourceConfig() {
        try {
            Properties properties = new Properties();
            properties.load(DataSourceConfig.class.getResourceAsStream("db.properties"));
            Set<Object> keySet = properties.keySet();
            for (Object key : keySet) {

                String fieldName = key.toString().split("\\.")[1];
                String fieldValue = properties.getProperty(key.toString());
                System.out.println(key + ":" + properties.get(key));
                //通过反射的方式给对象赋值

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
