package org.apache.ibatis.session;


/**
 * SqlSessionFactory工厂
 */
public interface SqlSessionFactory {

    // 获取SqlSession
    SqlSession openSession();

}
