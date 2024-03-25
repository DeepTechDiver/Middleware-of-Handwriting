package com.example.test;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.util.List;

public class TestDom4J {
    @Test
    public void testDom4J() {
        try {
            //创建一个解析器
            SAXReader reader = new SAXReader();
            //获取一个文档对象
            Document document = reader.read("D:\\1StudyFiles\\IDEA\\hand-write-ssm\\hand-write-mybatis\\src\\test\\resources\\testxml.xml");
            //获取xml文件的根节点
            Element rootElement = document.getRootElement();
            System.out.println("根节点的名字是:" + rootElement.getName());
            //获取子节点的集合
            List<Element> elements = rootElement.elements();
            System.out.println("子节点的个数为:" + elements.size());
            for (Element element : elements) {
                System.out.println("子节点的名字是:" + element.getName());
                Attribute attribute = element.attribute("id");
                String value = attribute.getValue();
                System.out.println("id的值是:" + value);
                //testxml节点下的子节点的集合
                List<Element> children = element.elements();
                for (Element child : children) {
                    //标签的名字
                    String tagName = child.getName();
                    //标签内部的值
                    String text = child.getText();
                    System.out.println(tagName + ":" + text);
                }
                System.out.println("-------------------");

            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
