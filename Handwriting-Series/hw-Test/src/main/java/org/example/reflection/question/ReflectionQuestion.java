package org.example.reflection.question;

import org.example.Cat;

import java.io.FileInputStream;
import java.util.Properties;

@SuppressWarnings("all")
public class ReflectionQuestion {

    public static void main(String[] args) throws Exception {
        // 1. 创建对象
        Cat cat = new Cat();
        cat.hi();

        Properties properties = new Properties();
        properties.load(new FileInputStream("re.properties"));
        String classpath = properties.get("classfullpath").toString();
        String method = properties.get("method").toString();
        System.out.println(classpath + " " + method);

    }
}
