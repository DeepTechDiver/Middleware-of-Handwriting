package org.apache.ibatis.utils;


import java.util.ArrayList;
import java.util.List;

/**
 * SQL语句中的参数解析
 */
public class ParameterMappingTokenHandler implements TokenHandler{

    private List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();

    @Override
    public String handleToken(String content) {
        ParameterMapping parameterMapping = new ParameterMapping(content);
        parameterMappings.add(parameterMapping);

        return "?";
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(List<ParameterMapping> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }
}
