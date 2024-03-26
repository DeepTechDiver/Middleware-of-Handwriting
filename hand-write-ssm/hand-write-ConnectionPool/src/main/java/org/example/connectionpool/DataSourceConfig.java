package org.example.connectionpool;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Set;
import java.lang.reflect.Field;

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
    private String waittime = "1000";

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
            properties.load(DataSourceConfig.class.getClassLoader().getResourceAsStream("db.properties"));
            // 实现了从 Properties 对象中获取所有键的集合
            // Properties 类继承自 Hashtable，因此它具有 keySet() 方法，该方法返回一个包含了所有键的集合，其类型为 Set<Object>
            Set<Object> keySet = properties.keySet();
            for (Object key : keySet) {

                String fieldName = key.toString().split("\\.")[1];
                String fieldValue = properties.getProperty(key.toString());
                //System.out.println(key + ":" + properties.get(key));
                //通过反射的方式给对象赋值
                setField(fieldName, fieldValue);

                //方法二
                //setField2(fieldName, fieldValue);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置类中的私有字段的值
     * @param fieldName 字段（即属性）
     * @param fieldValue 指定的值
     */
    private void setField(String fieldName, String fieldValue) {
        try {
            Field field = DataSourceConfig.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(this, fieldValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error setting field " + fieldName, e);
        }
    }


    /**
     *  方法二 反射获取Setxxx方法，并反射调用方法赋值
     * @param fieldName
     * @param fieldValue
     */
    private void setField2(String fieldName, String fieldValue) {
        try {
            Field field = DataSourceConfig.class.getDeclaredField(fieldName);
            Method method = DataSourceConfig.class.getDeclaredMethod(toUpper(fieldName), field.getType());
            //反射调用方法赋值
            method.invoke(this, fieldValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取配置文件中的key,并把他转成正确的set方法
     * @param fieldName 配置文件中的key
     * @return setXXX方法 例如生成了setDriver()方法
     */
    public String toUpper(String fieldName) {
        char[] chars = fieldName.toCharArray();
        chars[0] -= 32;     // 如何把一个字符串的首字母变成大写
        return "set" + new String(chars);
    }


    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInitSize() {
        return initSize;
    }

    public void setInitSize(String initSize) {
        this.initSize = initSize;
    }

    public String getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(String maxSize) {
        this.maxSize = maxSize;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getWaittime() {
        return waittime;
    }

    public void setWaittime(String waittime) {
        this.waittime = waittime;
    }
}
