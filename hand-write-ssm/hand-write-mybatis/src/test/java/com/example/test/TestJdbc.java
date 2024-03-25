package com.example.test;


import com.example.pojo.User;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class TestJdbc {

    /**
     * 测试jdbc
     */
    @Test
    public void testJdbc() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> users = new ArrayList<User>();

        try {
            //加载驱动、用于注册JDBC驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //获取连接对象
            connection =  DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/basic?characterEncoding=utf-8",
                    "root", "123456"
            );
            //获取到PreparedStatement对象
            preparedStatement = connection.prepareStatement("select * from user");
            //执行SQL语句返回结果集
            resultSet = preparedStatement.executeQuery();
            //遍历对象
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User user = new User(id, username, password);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null){
                    resultSet.close();
                }
                if (preparedStatement != null){
                    preparedStatement.close();
                }
                if (connection != null){
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("查询到的数据结果是:"+ users);
    }


    /**
     * 测试jdbc插入数据
     */
    @Test
    public void testJdbcInster() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> users = new ArrayList<User>();

        try {
            //加载驱动、用于注册JDBC驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //获取连接对象
            connection =  DriverManager.getConnection("jdbc:mysql://localhost:3306/basic?characterEncoding=utf-8",
                    "root", "123456");
            //获取到PreparedStatement对象  // mybatis 底层做了替换，将sql的# 替换成 ？
            preparedStatement = connection.prepareStatement("insert into user values (null,?,null)");
            preparedStatement.setObject(1,"Bob");
            //执行SQL语句返回结果集
            int count = preparedStatement.executeUpdate();
            System.out.println(count >0 ? "插入成功":"插入失败");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null){
                    resultSet.close();
                }
                if (preparedStatement != null){
                    preparedStatement.close();
                }
                if (connection != null){
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("查询到的数据结果是:"+ users);
    }

}
