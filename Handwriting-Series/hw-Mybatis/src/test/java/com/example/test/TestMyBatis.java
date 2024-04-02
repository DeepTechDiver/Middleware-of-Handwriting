package com.example.test;

import com.example.dao.UserDao;
import com.example.mapper.UserMapper;
import com.example.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
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
     public void init() throws Exception {
         //从XML中构建SqlSessionFactory
         String resource = "mybatis-config.xml";
         InputStream inputStream = Resources.getResourceAsStream(resource);
         SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
         SqlSession sqlSession = sqlSessionFactory.openSession();
         System.out.println(sqlSession);
         //框架底层使用JDK动态代理给接口生成实现类对象
         userMapper = sqlSession.getMapper(UserMapper.class);
     }

    @Test
    public void testList() {
        List<User> users = userMapper.list();
        users.forEach(System.out::println);
    }

    @Test
    public void testFindById() {
        User users = userMapper.findById(1);
        System.out.println(users);
    }

    @Test
    public void testInsert() {
        User user = new User();
        user.setUsername("新增测试12");
        user.setPassword("123456");
        Integer count = userMapper.insert(user);
        System.out.println(count>0? "新增成功":"新增失败");
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(11);
        user.setUsername("更新测试12");
        user.setPassword("654321");
        Integer count = userMapper.update(user);
        System.out.println(count>0? "更新成功":"更新失败");
    }

    @Test
    public void testDelete() {
        Integer count = userMapper.delete(11);
        System.out.println(count>0? "删除成功":"删除失败");
    }

}