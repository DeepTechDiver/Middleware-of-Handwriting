package org.apache.ibatis.utils;

public interface TokenHandler {

    /**
     * 对 token 进行解析
     * @param content 待解析 token
     * @return
     */
    String handleToken(String content);

}
