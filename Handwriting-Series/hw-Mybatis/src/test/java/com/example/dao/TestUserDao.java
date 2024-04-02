package com.example.dao;

import com.example.pojo.User;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 通过JDK动态代理给接口生成实现类
 */
public class TestUserDao {

    @Test
    public void testUserDao() {
        Class c = UserDao.class;
        MyInvocationHandler myInvocationHandler = new MyInvocationHandler();

        //生成一个代理类对象
        UserDao userDao = (UserDao) Proxy.newProxyInstance(c.getClassLoader(), new Class[]{c}, myInvocationHandler);
        //此时 userDao 已经不是接口而是动态代理的对象
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
        if (method.getName().equals("add")){
            //调用目标方法的add
            System.out.println("执行ADD的参数："+ args[0]);
            return 1;
        }else {
            return null;
        }
//        return null;
    }
}