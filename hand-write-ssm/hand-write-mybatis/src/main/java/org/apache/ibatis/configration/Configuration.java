package org.apache.ibatis.configration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.connectionpool.DataSourceConfig;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configuration类是MyBatis框架的SQL配置封装类，后期使用Dom4]进行解析
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {

    /**
     * 数据源
     */
    private DataSourceConfig dataSource;

    /**
     * 封装的UserMaper.xml文件中的SQL语句，因为maper.xml文件中不止一条SQL
     */
    Map<String, MappedStatement> mappedStatementMap = new ConcurrentHashMap<String,MappedStatement>();

}
