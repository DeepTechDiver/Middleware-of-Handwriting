package org.apache.ibatis.utils;

/**
 * 解析SQL语句
 */
public class GenericTokenParser {

    //开始标记
    private String openToken;
    //结束标记
    private String closeToken;
    //标记处理器
    private TokenHandler handler;

    public GenericTokenParser(String openToken, String closeToken, TokenHandler handler) {
        this.openToken = openToken;
        this.closeToken = closeToken;
        this.handler = handler;
    }


    /**
     * 解析#{ }
     * 实现主要配置文件中的#{}占位符的解析工作
     * 处理工作是由处理器handler的handleToken()方法来实现
     * @param text
     * @return
     */
    public String parse(String text) {
        // 验证参数问题，如果是null，就返回空字符串。
        if (text == null || text.isEmpty()) {
            return "";
        }
        // 下面继续验证是否包含开始标签，如果不包含，默认不是占位符，直接原样返回即可，否则继续执行
        int start = text.indexOf(openToken);
        if (start == -1) {
            // 没有 token 前缀，返回原文本
            return text;
        }
        // 把text转成字符数组src，并且定义默认偏移量offset=0、存储最终需要返回字符串的变量builder，
        // text变量中占位符对应的变量名expression。判断start是否大于-1(即text中是否存在openToken)，如果存在就执行下面代码
        char[] src = text.toCharArray();
        // 当前解析偏移量
        int offset = 0;
        // 已解析文本
        final StringBuilder builder = new StringBuilder();
        // 当前占位符内的表达式
        StringBuilder expression = null;
        while (start > -1) {
            if (start > 0 && src[start - 1] == '\\') {
                // 如果待解析属性前缀被转义，则去掉转义字符，加入已解析文本
                // 判断如果开始标记前如果有转义字符，就不作为openToken进行处理，否则继续处理
                builder.append(src, offset, start - offset - 1).append(openToken);
                // 更新解析偏移量
                offset = start + openToken.length();
            } else {
                //重置expression变量，避免空指针或者老数据干扰。
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                // 前缀前面的部分加入已解析文本
                builder.append(src, offset, start - offset);
                // 更新解析偏移量
                offset = start + openToken.length();
                // 获取对应的后缀索引
                int end = text.indexOf(closeToken, offset);
                while (end > -1) { //存在结束标记时
                    if (end > offset && src[end - 1] == '\\') { //如果结束标记前面有转义字符时
                        // 后缀被转义，加入已解析文本
                        // this close token is escaped. remove the backslash and continue.
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        offset = end + closeToken.length();
                        // 寻找下一个后缀
                        end = text.indexOf(closeToken, offset);
                    } else { //不存在转义字符，即需要作为参数进行处理
                        // 找到后缀，获取占位符内的表达式
                        expression.append(src, offset, end - offset);
                        offset = end + closeToken.length();
                        break;
                    }
                }
                if (end == -1) {
                    // 找不到后缀，前缀之后的部分全部加入已解析文本
                    // close token was not found.
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {
                    // 能够找到后缀，追加 token 处理器处理后的文本
                    builder.append(handler.handleToken(expression.toString()));
                    // 更新解析偏移量
                    offset = end + closeToken.length();
                }
            }
            // 寻找下一个前缀，重复解析表达式
            start = text.indexOf(openToken, offset);
        }
        if (offset < src.length) {
            // 将最后的部分加入已解析文本
            builder.append(src, offset, src.length - offset);
        }
        // 返回解析后的文本
        return builder.toString();
    }
}
