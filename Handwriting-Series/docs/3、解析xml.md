# 手写Mybatis

### 1、自定义Resources类

自定义Resources类

Resources类的作用是获取一个类加载器，根据路径获取资源文件，并将配置文件加载为字节流的形式存储在内存中

```
public class Resources {

    /**
     * 根据路径获取资源文件，并将配置文件加载为字节流的形式存储在内存中
     * @param resource 配置文件路径
     * @return 返回的字节流
     */
    public static InputStream getResourceAsStream(String resource) {

        //加载类路径下的配置文件，字节流返回
        InputStream inputStream =  Resources.class.getClassLoader().getResourceAsStream(resource);
        return inputStream;
    }
}
```

### 2、自定义MappedStatement类

MappedStatement类作用是封装UserMapperxml文件解析之后的SQL语句信息，在底层框架可以使用Dom4]进行解析!

```
package org.apache.ibatis.configration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MyBatis-config.xml 配置文件 SQL语句映射
 * MappedStatement类作用是封装UserMapper.xml文件解析之后的SQL语句信息，在底层框架可以使用Dom4]进行解析!
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MappedStatement {

    /**
     * id表示 每条SQL的唯一标识
     */
    private String id;

    /**
     * SQL语句的返回值
     */
    private String resultType;

    /**
     * 参数类型
     */
    private String parameterType;

    /**
     * SQL语句
     */
    private String sql;

}
```



### 3、自定义Configuration类

Configuration类是MyBatis框架的SQL配置封装类，后期使用Dom4]进行解析

```
package org.apache.ibatis.configration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configuration类是MyBatis框架的SQL配置封装类，后期使用Dom4]进行解析
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {
    
    /**
     * 数据源
     */
    private DataSource dataSource;

    /**
     * 封装的UserMaper.xml文件中的SQL语句，因为maper.xml文件中不止一条SQL
     */
    Map<String, MappedStatement> mappedStatementMap = new ConcurrentHashMap<String,MappedStatement>();

}
```

### 4、解析mapper.xml

#### 4.1、解析mapper.xml

> 自定义类XMLMapperBuilder

```
package org.apache.ibatis.configration;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.security.sasl.Sasl;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用DOM4J解析XML配置文件
 */
public class XmlMapperBuilder {

    /**
     * 配置数据封装对象
     */
    private Configuration configuration;

    public XmlMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 传入配置文件的字节流，解析配置文件
     * @param inputStream
     */
    public void parse(InputStream inputStream) throws DocumentException {
        // 1. 获取XML配置文件
        // 2. 解析XML配置文件
        //创建解析器对象
        SAXReader saxReader = new SAXReader();
        //把XML加载成Document文档对象
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();
        Attribute attribute = rootElement.attribute("namespace");
        //获取到com.example.mapper.UserMapper
        String namespace = attribute.getValue();
        //检索数据
        //xpath解析,解析XML配置文件
        List<Element> selectList =  rootElement.selectNodes("//select");
        List<Element> insertList =  rootElement.selectNodes("//insert");
        List<Element> updateList =  rootElement.selectNodes("//update");
        List<Element> deleteList =  rootElement.selectNodes("//delete");

        List<Element> allList = new ArrayList<Element>();
        allList.addAll(selectList);
        allList.addAll(insertList);
        allList.addAll(updateList);
        allList.addAll(deleteList);

        for (Element element : allList) {
            //获取每一条id的返回值
            String id = element.attributeValue("id");
            //获取返回值类型 (增删改都是Integer类型)
            String resultType = element.attributeValue("resultType");
            //获取参数类型
            String parameterType = element.attributeValue("parameterType");
            //获取每个mapper节点中的SQL语句
            String sqlText = element.getTextTrim();
            //封装对象
            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setResultType(resultType);
            mappedStatement.setParameterType(parameterType);
            mappedStatement.setSql(sqlText);

            // 3. 封装Configuration对象
            //key : com.example.mapper.UserMapper.list/findById/add/update/delete
            String key = namespace+"."+id;
            configuration.getMappedStatementMap().put(key, mappedStatement);
        }
    }
}
```

通过调用 parse函数 传入一个配置文件的字节流，返回得到一个Configuration对象

通过编写测试Debug可以查看代码解析的结果:

```
@Test
public void testMapper(){
    try {
        //MyBatis配置类
        Configuration configuration = new Configuration();
        InputStream inputStream =  Resources.getResourceAsStream("mappers/UserMapper.xml");
        XmlMapperBuilder xmlMapperBuilder = new XmlMapperBuilder(configuration);
        xmlMapperBuilder.parse(inputStream);
        System.out.println(configuration);
    } catch (DocumentException e) {
        throw new RuntimeException(e);
    }

}
```

![image-20240325153120502](D:\1StudyFiles\IDEA\hand-write-ssm\docs\image-20240325153120502.png)



#### 4.1、解析mapper.xml

```

```

