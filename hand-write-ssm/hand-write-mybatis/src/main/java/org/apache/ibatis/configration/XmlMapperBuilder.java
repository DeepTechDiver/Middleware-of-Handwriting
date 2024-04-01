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
            //获取每一个SQL语句标签类型 {insert, update,delete,select}
            String sqlType = element.getName();

            //封装对象
            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setResultType(resultType);
            mappedStatement.setParameterType(parameterType);
            mappedStatement.setSql(sqlText);
            mappedStatement.setSqlType(sqlType);

            // 3. 封装Configuration对象
            //key : com.example.mapper.UserMapper.list/findById/add/update/delete
            String key = namespace+"."+id;
            configuration.getMappedStatementMap().put(key, mappedStatement);
        }
    }
}
