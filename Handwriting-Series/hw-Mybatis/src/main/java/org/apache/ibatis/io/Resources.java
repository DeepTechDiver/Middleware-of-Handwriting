package org.apache.ibatis.io;

import java.io.InputStream;

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
