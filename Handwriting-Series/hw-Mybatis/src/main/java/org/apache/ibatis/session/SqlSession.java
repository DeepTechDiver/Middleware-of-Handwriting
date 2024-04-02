package org.apache.ibatis.session;

import java.util.List;

/**
 * SqlSession接口
 */
public interface SqlSession {
    /**
     * 查询所有数据
     * @param statementId sql唯一id
     * @param params      sql有可能是模糊查询，传可变参数Object...
     * @param <E>         泛型
     * @return List集合
     */
    <E> List<E> selectList(String statementId, Object... params) throws Exception;

    /**
     * 根据条件查询单个
     * @param statementId sql唯一id
     * @param params      sql有可能是模糊查询，传可变参数
     * @param <T>         泛型
     * @return 某一对象
     */
    <T> T selectOne(String statementId, Object... params) throws Exception;


    /**
     * 新增数据
     * @param statementId sql唯一id
     * @param params      sql有可能是模糊查询，传可变参数
     * @param <T>         泛型
     * @return 某一对象
     */
    <T> T insert(String statementId, Object... params) throws Exception;


    /**
     * 更新操作
     *
     * @param statementId sql唯一id
     * @param params      可变参数
     * @return 更新条数
     */
    <T> T update(String statementId, Object... params) throws Exception;

    /**
     * 删除数据操作
     *
     * @param statementId sql唯一id
     * @param params      可变参数
     * @return 更新条数
     */
    <T> T delete(String statementId, Object... params) throws Exception;

    /**
     * 为Dao层接口生成代理实现类
     * 通过class动态代理生成Dao层接口的代理实现类
     * @param mapperClass 字节码
     * @param <T>         泛型
     * @return 接口的代理类的某一个对象
     */
    <T> T getMapper(Class<?> mapperClass) throws Exception;
}
