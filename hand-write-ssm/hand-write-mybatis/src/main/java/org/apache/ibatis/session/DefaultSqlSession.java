package org.apache.ibatis.session;

import org.apache.ibatis.configration.CommandType;
import org.apache.ibatis.configration.Configuration;
import org.apache.ibatis.configration.MappedStatement;
import org.apache.ibatis.executor.SimpleExecutor;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DefaultSqlSession  implements  SqlSession{

    //封装的是配置信息
    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {
        // 将要去完成对SimpleExecutor里的query方法的调用
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = this.configuration.getMappedStatementMap().get(statementId);
        return simpleExecutor.query(configuration, mappedStatement, params);
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<T> list = this.selectList(statementId, params);
        if (list.size() == 1){
            return list.get(0);
        }else {
            throw  new RuntimeException("查询结果为空或者返回结果过多");
        }
    }

    @Override
    public <T> T insert(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = this.configuration.getMappedStatementMap().get(statementId);
        List<T> counts = simpleExecutor.query(configuration, mappedStatement, params);
        if (counts.size() == 1){
            return counts.get(0);
        }else {
            return (T) Integer.valueOf(0);
        }
    }

    @Override
    public <T> T update(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = this.configuration.getMappedStatementMap().get(statementId);
        return (T) simpleExecutor.query(configuration, mappedStatement, params).get(0);
    }

    @Override
    public <T> T delete(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = this.configuration.getMappedStatementMap().get(statementId);
        return (T) simpleExecutor.query(configuration, mappedStatement, params).get(0);
    }

    /**
     * 使用JDK动态代理给UserMapper接口生成代理对象
     * @param mapperClass 字节码
     * @return
     * @param <T>
     * @throws Exception
     */
    @Override
    public <T> T getMapper(Class<?> mapperClass) throws Exception {
        //直接new接口的话，是匿名内部类（不用单独在外面定义一个类），无法获取接口的泛型
        Object instance = Proxy.newProxyInstance(mapperClass.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                //接口中的方法名字
                String methodName = method.getName();
                //接口的全类名
                String className = method.getDeclaringClass().getName();
                //拼接SQL的唯一标识
                String statmentId = className + "." + methodName;
                //获取方法被调用之后的返回类型
                Type genericReturnType = method.getGenericReturnType();

                Map<String, MappedStatement> mappedStatementMap = configuration.getMappedStatementMap();
                MappedStatement mappedStatement = mappedStatementMap.get(statmentId);

                /**
                 * .contains("update")通常用于检查字符串中是否包含特定的子字符串。
                 * "update".equals()用于检查字符串是否与给定的字符串相等。
                 */
                //通过xml标签来判断增删改的操作
                if ("insert".equals(mappedStatement.getSqlType())){
                    return insert(statmentId, args);
                }else if ("delete".equals(mappedStatement.getSqlType())){
                    return delete(statmentId, args);
                } else if ("update".equals(mappedStatement.getSqlType())){
                    return update(statmentId, args);
                }

                //修改方法
//                if (methodName.contains(CommandType.insert.toString())){
//                    return insert(statmentId, args);
//                }else if (methodName.contains(CommandType.delete.toString())){
//                    return delete(statmentId, args);
//                } else if (methodName.contains(CommandType.update.toString())){
//                    return update(statmentId, args);
//                }
                //判断是否是集合即是否进行泛型类型的参数化（返回结果是否为泛型 是则查询集合 ）
                if (genericReturnType instanceof ParameterizedType){
                    List<Object> objects = selectList(statmentId, args);
                    return objects;
                }else {
                    //查询结果不是泛型
                    return selectOne(statmentId, args);
                }
            }
        });
        return (T) instance;
    }
}
