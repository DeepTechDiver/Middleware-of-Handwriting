# 手写Mybatis

## 回顾Jdbc

第一步、 新建项目：

第二步、新建项目目录：

包含一个父工程hand-write-ssm以及一个子模块hand-write-mybatis：

| ![image-20240325105256440](D:\1StudyFiles\IDEA\hand-write-ssm\docs\image-20240325105256440.png) |
| ------------------------------------------------------------ |

新建测试，添加测试junit依赖

```
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
</dependency>
```





```
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
```

