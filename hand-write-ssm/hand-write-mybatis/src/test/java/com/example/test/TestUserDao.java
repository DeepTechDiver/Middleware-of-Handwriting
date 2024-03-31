package com.example.test;

import com.example.dao.UserDao;
import com.example.mapper.UserMapper;
import com.example.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;

public class TestUserDao {

    UserDao userMapper;

    @Test
    public void testUserDao() throws Exception {
        //从XML中构建SqlSessionFactory
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        System.out.println(sqlSession);
//         //框架底层使用JDK动态代理给接口生成实现类对象
        userMapper = sqlSession.getMapper(UserDao.class);
        System.out.println(inputStream);

        int count =  userMapper.add(new User(1, "张三", "123456"));
        System.out.println(count);
    }
}
