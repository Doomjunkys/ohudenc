package org.itkk.udf.dal.mybatis.plugin;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.itkk.udf.dal.mybatis.plugin.pagequery.BoundSqlSqlSource;

import java.util.Arrays;
import java.util.List;

/**
 * 描述 : InterceptorUtil
 *
 * @author wangkang
 */
public class InterceptorUtil {

    /**
     * 描述 : 构造函数
     */
    private InterceptorUtil() {

    }

    /**
     * 复制MappedStatement
     *
     * @param mappedStatement   mappedStatement
     * @param boundSql          boundSql
     * @param sql               sql
     * @param parameterMappings parameterMappings
     * @param parameter         parameter
     * @return 结果
     */
    public static MappedStatement copyFromNewSql(MappedStatement mappedStatement, BoundSql boundSql, String sql,
                                                 List<ParameterMapping> parameterMappings, Object parameter) {
        BoundSql newBoundSql = copyFromBoundSql(mappedStatement, boundSql, sql, parameterMappings, parameter);
        return copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));
    }

    /**
     * 复制MappedStatement
     *
     * @param ms           MappedStatement
     * @param newSqlSource newSqlSource
     * @return 结果
     */
    private static MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        Builder builder = new Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        String[] keys = ms.getKeyProperties();
        if (keys != null) {
            String keysstr = Arrays.toString(keys);
            keysstr = keysstr.replace("[", "");
            keysstr = keysstr.replace("]", "");
            builder.keyProperty(keysstr);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        builder.databaseId(ms.getDatabaseId());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        String[] keyColumns = ms.getKeyColumns();
        if (keyColumns != null) {
            String keysstr = Arrays.toString(keyColumns);
            keysstr = keysstr.replace("[", "");
            keysstr = keysstr.replace("]", "");
            builder.keyColumn(keysstr);
        }
        builder.lang(ms.getLang());
        String[] resulSets = ms.getResultSets();
        if (resulSets != null) {
            String keysstr = Arrays.toString(resulSets);
            keysstr = keysstr.replace("[", "");
            keysstr = keysstr.replace("]", "");
            builder.resultSets(keysstr);
        }
        builder.resultSetType(ms.getResultSetType());
        builder.statementType(ms.getStatementType());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    /**
     * 复制MappedStatement
     *
     * @param mappedStatement   mappedStatement
     * @param boundSql          boundSql
     * @param sql               sql
     * @param parameterMappings parameterMappings
     * @param parameter         parameter
     * @return 结果
     */
    private static BoundSql copyFromBoundSql(MappedStatement mappedStatement, BoundSql boundSql, String sql,
                                             List<ParameterMapping> parameterMappings, Object parameter) {
        BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), sql, parameterMappings, parameter);
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

}
