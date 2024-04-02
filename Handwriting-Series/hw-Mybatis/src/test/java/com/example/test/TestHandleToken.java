package com.example.test;

import org.apache.ibatis.configration.BoundSql;
import org.apache.ibatis.utils.GenericTokenParser;
import org.apache.ibatis.utils.ParameterMappingTokenHandler;
import org.junit.Test;

public class TestHandleToken {

    @Test
    public void testHandleToken(){
//        String sql = "select * from user where id = #{id} and name = #{name}";
        String sql = "insert into user values (null,#{username},#{password})";
        ParameterMappingTokenHandler parameterMapping = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMapping);
        String parse = genericTokenParser.parse(sql);
        BoundSql boundSql = new BoundSql();
        boundSql.setSqlText(parse);
        boundSql.setParameterMappings(parameterMapping.getParameterMappings());
        System.out.println(boundSql);
    }
}
