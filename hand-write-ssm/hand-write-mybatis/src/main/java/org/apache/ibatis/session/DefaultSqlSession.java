package org.apache.ibatis.session;

import org.apache.ibatis.configration.Configuration;

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
        return null;
    }
}
