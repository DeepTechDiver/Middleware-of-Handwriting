package org.apache.ibatis.session;

import org.apache.ibatis.configration.CommandType;
import org.apache.ibatis.configration.Configuration;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

public class DefaultSqlSession  implements  SqlSession{

    //封装的是配置信息
    private Configuration configuration;
    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {
        return null;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        return null;
    }

    @Override
    public <T> T insert(String statementId, Object... params) throws Exception {
        return null;
    }

    @Override
    public <T> T update(String statementId, Object... params) throws Exception {
        return null;
    }

    @Override
    public <T> T delete(String statementId, Object... params) throws Exception {
        return null;
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
//                if (Arrays.asList(CommandType.INSERT).contains(methodName)){
//                    //在JDBC中执行insert,delete,update都是在执行update
//                    return update(methodName);
//                }
                if (methodName.contains(CommandType.INSERT.toString())){
                    return insert(statmentId, args);
                }else if (methodName.contains(CommandType.DELETE.toString())){
                    return delete(statmentId, args);
                } else if (methodName.contains(CommandType.UPDATE.toString())){
                    return update(statmentId, args);
                }
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
