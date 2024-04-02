## Dom4J

通过 Dom4J开源工具来读取xml文件获取到xml文件的内容



testxml.xml文件内容;

```
<?xml version="1.0" encoding="UTF-8"?>

<testxml>
    <user id="U1">
        <id>1</id>
        <username>zhangsan</username>
        <password>123456</password>
    </user>
    <user id="U2">
        <id>2</id>
        <username>Jack</username>
        <password>123456</password>
    </user>
</testxml>
```



测试文件TestDom4J内容：

```
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
```

```
根节点的名字是:testxml
子节点的个数为:2
子节点的名字是:user
id的值是:U1
id:1
username:zhangsan
password:123456
-------------------
子节点的名字是:user
id的值是:U2
id:2
username:Jack
password:123456
-------------------
```

