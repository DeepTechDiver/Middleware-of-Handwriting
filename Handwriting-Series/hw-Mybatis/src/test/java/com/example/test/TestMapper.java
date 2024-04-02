package com.example.test;

import org.apache.ibatis.configration.Configuration;
import org.apache.ibatis.configration.XmlMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.io.InputStream;

public class TestMapper {

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
}
