package org.apache.ibatis.executor;

import org.apache.ibatis.configration.BoundSql;
import org.apache.ibatis.configration.CommandType;
import org.apache.ibatis.configration.Configuration;
import org.apache.ibatis.configration.MappedStatement;
import org.apache.ibatis.utils.GenericTokenParser;
import org.apache.ibatis.utils.ParameterMapping;
import org.apache.ibatis.utils.ParameterMappingTokenHandler;
import org.example.connectionpool.DataSourceConfig;
import org.example.connectionpool.DataSourceManager;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SQL语句执行器
 */
public class SimpleExecutor implements Executor {

    /**
     * 执行SQL的核心方法（最终执行JDBC的方法）
     * @param configuration
     * @param mappedStatement
     * @param params
     * @return
     * @param <E>
     */
    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {

        // 1.获取数据库的连接

//        Connection connection = configuration.getDataSource().getConnection();
        Connection connection = DataSourceManager.getConn();


        System.out.println(connection);
        // 2.获取要执行的SQL语句 （select * from user where id=#{id} 解析为 select * from user where id=?）
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);
        // 3.获取预处理对象：preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        // 4.设置参数
        String parameterType = mappedStatement.getParameterType();
        Class<?> parameterTypeClass = this.getClassType(parameterType);
        if (parameterTypeClass != null){
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                String content = parameterMapping.getContent();
                //暴力反射
                Field declaredField = parameterTypeClass.getDeclaredField(content);
                // 暴力访问，防止访问的字段是private修饰
                declaredField.setAccessible(true);
                Object o = declaredField.get(params[0]);
                preparedStatement.setObject(i+1,o);
            }
        }
        // 5.执行sql
        String id = mappedStatement.getId();
        ResultSet resultSet = null;
        //增删改
//            if (!Arrays.asList(CommandType.sqlCommand).contains(id))
        if (
            id.contains(CommandType.DELETE.toString()) ||
            id.contains(CommandType.UPDATE.toString()) ||
            id.contains(CommandType.INSERT.toString())
        ){
            Integer count = preparedStatement.executeUpdate();
            List<Integer> counts = new ArrayList<>();
            counts.add(count);
            return (List<E>) counts;
        }else {
            //查询
            resultSet = preparedStatement.executeQuery();
        }

        //获取到返回值类型
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);
        List<Object> objects = new ArrayList<>();

        // 6.封装返回结果集
        while (resultSet.next()) {
            //调无无参的方法生成对象
            Object o = resultTypeClass.newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i < metaData.getColumnCount(); i++) {
                //获取字段名
                String columnName = metaData.getColumnName(i);
                // 字段的值
                Object value = resultSet.getObject(columnName);
                // 使用反射或者内省，根据数据库表和实体的对应关系，完成封装
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, value);
            }
            objects.add(o);
        }

        return (List<E>) objects;
    }

    public Class<?> getClassType(String parameterType) {
        try {
            if (parameterType != null){
                return Class.forName(parameterType);
            }
            return null;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 完成对#{}的解析工作:
     * 1.将#{}使用？进行代替
     * 2.解析出#{}里面的值进行存储
     * @param sql 原生sql
     * @return    解析后的sql
     */
    private BoundSql getBoundSql(String sql){
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        // String s = parameterMappingTokenHandler.handleToken(sql);
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String parse = genericTokenParser.parse(sql);
        BoundSql boundSql = new BoundSql();
        boundSql.setSqlText(parse);
        boundSql.setParameterMappings(parameterMappingTokenHandler.getParameterMappings());
        return boundSql;
    }
}
