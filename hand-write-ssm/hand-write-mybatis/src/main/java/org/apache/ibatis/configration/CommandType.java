package org.apache.ibatis.configration;

/**
 * SQL类型
 */
public enum CommandType {

    INSERT("insert"),
    UPDATE("update"),
    DELETE("delete");

    private String value;

    CommandType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

//public class CommandType {
//
//    public final static String[] sqlCommand = {"insert","update","delete"};
//}
