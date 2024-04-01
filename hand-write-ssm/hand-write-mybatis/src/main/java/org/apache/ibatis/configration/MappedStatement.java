package org.apache.ibatis.configration;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MyBatis-config.xml 配置文件 SQL语句映射
 * MappedStatement类作用是封装UserMapper.xml文件解析之后的SQL语句信息，在底层框架可以使用Dom4]进行解析!
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MappedStatement {

    /**
     * id表示 每条SQL的唯一标识
     */
    private String id;

    /**
     * SQL语句的返回值
     */
    private String resultType;

    /**
     * 参数类型
     */
    private String parameterType;

    /**
     * SQL语句
     */
    private String sql;

    /**
     * 封装SQL语句的参数
     */
    private String sqlType;

}
