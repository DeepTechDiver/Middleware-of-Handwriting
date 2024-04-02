package com.example.test;

import org.apache.ibatis.configration.Configuration;
import org.apache.ibatis.configration.XmlConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.io.InputStream;

public class TestXmlConfigBuilder {
    @Test
    public void testXmlConfigBuilder()  {
        try {
            Configuration configuration = new Configuration();
            XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(configuration);
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            Configuration configuration1 = xmlConfigBuilder.parseMyBatisConfig(inputStream);
            System.out.println("配置文件解析完毕:" + configuration1);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
