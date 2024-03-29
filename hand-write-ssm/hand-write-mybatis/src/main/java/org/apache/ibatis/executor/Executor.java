package org.apache.ibatis.executor;

import org.apache.ibatis.configration.Configuration;
import org.apache.ibatis.configration.MappedStatement;
import java.util.List;

/**
 * SQL语句的执行器 Executor执行器接口
 */
public interface Executor {

    /**
     * SQL语句执行器
     * @param configuration
     * @param ms
     * @param params
     * @return
     * @param <E>
     */
    <E>List<E> query(Configuration configuration, MappedStatement ms, Object... params) throws Exception;
}
