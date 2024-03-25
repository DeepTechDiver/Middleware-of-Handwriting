package com.example.test;

import com.example.mapper.UserMapper;
import com.example.pojo.User;
import org.junit.Before;
import org.junit.Test;
import org.apache.ibatis.io.Resources;

import java.io.InputStream;
import java.util.List;

public class TestMyBatis {

    UserMapper userMapper;

     /**
     *在单元测试之前执行
     */
     @Before
     public void init() {
         //从XML中构建SqlSessionFactory
         String resource = "mybatis-config.xml";
         InputStream inputStream = Resources.getResourceAsStream(resource);
//         SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//         SqlSession sqlSession = sqlSessionFactory.openSession();
//         //框架底层使用JDK动态代理给接口生成实现类对象
//         userMapper = sqlSession.getMapper(UserDao.class);
         System.out.println(inputStream);
     }

    @Test
    public void testMyBatis() {
//        List<User> users = userMapper.list();
//        users.forEach(System.out::println);
    }
}