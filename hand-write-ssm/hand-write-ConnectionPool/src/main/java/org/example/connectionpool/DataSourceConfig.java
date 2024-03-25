package org.example.connectionpool;

import java.io.IOException;
import java.util.Properties;

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


    public static DataSourceConfig instance = null;

    static {
        try {
            Properties properties = new Properties();
            properties.load(DataSourceConfig.class.getResourceAsStream("db.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static DataSourceConfig getInstance() {
        if (instance == null) {
            synchronized (DataSourceConfig.class) {
                if (instance == null) {
                    instance = new DataSourceConfig();
                }
            }
        }
        return null;
    }

}
