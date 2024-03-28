package org.apache.ibatis.configration;

import org.apache.ibatis.io.Resources;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.example.connectionpool.DataSourceConfig;
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
     * 解析MyBatis-config配置文件 ： 使用dom4j对配置文件进行解析，封装成Configuration对象
     * @return Configuration对象
     */
    public Configuration parseMyBatisConfig(InputStream inputStream) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document =  saxReader.read(inputStream);
        Element rootElement = document.getRootElement();

        //映射出xml的dataSource中的property配置
        List<Element> propertyList = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        for (Element element : propertyList) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name,value);
        }

        //初始化一个数据库连接池
        //Todo 手写连接池 1.0


        DataSourceConfig.getInstance().setDriver(properties.getProperty("driverClass"));
        DataSourceConfig.getInstance().setUrl(properties.getProperty("url"));
        DataSourceConfig.getInstance().setUsername(properties.getProperty("username"));
        DataSourceConfig.getInstance().setPassword(properties.getProperty("password"));

        //映射XxxMapper.xml文件
        List<Element> mapperlist = rootElement.selectNodes("//mapper");
        for (Element element : mapperlist) {
            String resource = element.attributeValue("resource");
            InputStream resourceAsStream = Resources.getResourceAsStream(resource);
            XmlMapperBuilder xmlMapperBuilder = new XmlMapperBuilder(configuration);
            xmlMapperBuilder.parse(resourceAsStream);
        }


        return configuration;
    }
}
