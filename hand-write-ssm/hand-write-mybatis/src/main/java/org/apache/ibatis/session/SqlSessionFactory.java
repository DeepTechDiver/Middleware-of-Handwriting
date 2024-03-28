package org.apache.ibatis.session;


/**
 * SqlSessionFactory工厂
 */
public interface SqlSessionFactory {
    SqlSession openSession();

}
