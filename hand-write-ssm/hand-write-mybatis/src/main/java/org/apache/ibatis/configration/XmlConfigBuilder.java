package org.apache.ibatis.configration;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * 解析MyBatis的核心配置文件 mybatis-config.xml
 */
public class XmlConfigBuilder {

    private Configuration configuration;

    public XmlConfigBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 解析MyBatis-config配置文件
     * @return
     */
    public Configuration parseMyBatisConfig(InputStream inputStream) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document =  saxReader.read(inputStream);
        Element rootElement = document.getRootElement();
        List<Element> propertyList = rootElement.selectNodes("property");
        Properties properties = new Properties();
        for (Element element : propertyList) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name,value);
        }

        //初始化一个数据库连接池
        //Todo 手写连接池



        return null;
    }
}
