package org.apache.ibatis.session;

import org.apache.ibatis.configration.Configuration;
import org.apache.ibatis.configration.XmlConfigBuilder;
import org.dom4j.DocumentException;

import java.io.InputStream;

/**
 * 生成一个SqlSessionFactory对象
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream inputStream) {
        try {
            //获取configuration对象
            Configuration configuration = new Configuration();
            XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(configuration);
            xmlConfigBuilder.parseMyBatisConfig(inputStream);
            //创建SqlSessionFactory对象
            DefaultSqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
            return sqlSessionFactory;
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
