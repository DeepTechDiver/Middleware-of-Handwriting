package com.example.dao;

import com.example.pojo.User;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TestUserDao {

    @Test
    public void testUserDao() {
        Class c = UserDao.class;
        MyInvocationHandler myInvocationHandler = new MyInvocationHandler();

        //生成一个代理类对象
        UserDao userDao = (UserDao) Proxy.newProxyInstance(c.getClassLoader(), new Class[]{c}, myInvocationHandler);
        //userDao对象
        int count = userDao.add(new User(1, "Test","123456"));
        System.out.println(count>0?"添加成功":"添加失败");
    }
}


/**
 * 方法拦截器
 */
class MyInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}