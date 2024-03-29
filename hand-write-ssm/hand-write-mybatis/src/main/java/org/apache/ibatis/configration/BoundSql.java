package org.apache.ibatis.configration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.utils.ParameterMapping;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoundSql {

    //要执行的SQL语句(#转换成？完毕的sql)
    private String sqlText;

    //执行SQL语句参数的集合
    private List<ParameterMapping> parameterMappings;
}
