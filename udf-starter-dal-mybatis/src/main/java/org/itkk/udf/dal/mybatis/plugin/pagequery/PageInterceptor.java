package org.itkk.udf.dal.mybatis.plugin.pagequery;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.itkk.udf.core.exception.SystemRuntimeException;
import org.itkk.udf.dal.mybatis.MyBatisProperties;
import org.itkk.udf.dal.mybatis.plugin.InterceptorUtil;
import org.itkk.udf.dal.mybatis.plugin.pagequery.parser.SqlParser;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * <p>
 * ClassName: PageInterceptor
 * </p>
 * <p>
 * Description: 分页插件
 * </p>
 * <p>
 * Author: wangkang
 * </p>
 * <p>
 * Date: 2016年3月23日
 * </p>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
@Slf4j
public class PageInterceptor implements Interceptor {

    /**
     * 本地变量
     */
    private static final ThreadLocal<PageResult> LOCAL_PAGE = new ThreadLocal<>();

    /**
     * myBatisProperties
     */
    @Autowired
    private MyBatisProperties myBatisProperties;

    /**
     * 获取Page参数
     *
     * @return 分页对象
     */
    private static PageResult getLocalPage() {
        return LOCAL_PAGE.get();
    }

    /**
     * 设置Page参数
     *
     * @param page 分页结果
     */
    private static void setLocalPage(PageResult page) {
        LOCAL_PAGE.set(page);
    }

    /**
     * 移除本地变量
     */
    private static void clearLocalPage() {
        LOCAL_PAGE.remove();
    }


    /**
     * 开始分页
     *
     * @param pageSize 每页显示数量
     * @param curPage  页码
     */
    public static void startPage(int pageSize, int curPage) {

        // 规范参数
        if (pageSize <= 0) {
            throw new SystemRuntimeException("pageSize <= 0");
        }
        if (curPage <= 0) {
            throw new SystemRuntimeException("curpage <= 0");
        }

        // 实例化分页对象
        PageResult page = new PageResult();
        page.setPageSize(pageSize); // 设置每页条数
        page.setCurPage(curPage); // 设置当前页

        // 保存对象至本地变量中
        setLocalPage(page);

    }

    /**
     * 开始分页
     *
     * @param pageSize     每页数量
     * @param curPage      当前页数
     * @param totalRecords 总记录数
     */
    public static void startPage(int pageSize, int curPage, int totalRecords) {

        // 规范参数
        if (totalRecords < 0) {
            throw new SystemRuntimeException("totalRecords < 0");
        }

        // 设置总行数
        startPage(pageSize, curPage);
        PageResult page = getLocalPage();
        page.setTotalRecords(totalRecords);

        // 保存对象至本地变量中
        setLocalPage(page);

    }

    /**
     * 将结果集转换为PageResult
     *
     * @param result 结果集
     * @param <E>    泛型
     * @return 结果集
     */
    public static <E> Page<E> getPageResult(List<E> result) {
        return new Page((PageResult<E>) result);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            // 获得分页对象
            PageResult pr = getLocalPage();

            // 获取必要参数
            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            String id = mappedStatement.getId();
            log.debug("MappedStatement id = " + id);
            // 判断是否需要分页
            if (pr != null) {
                Object parameter = invocation.getArgs()[1];
                BoundSql boundSql = mappedStatement.getBoundSql(parameter);
                String sql = boundSql.getSql();
                // 获得链接
                Connection connection = getConnection(mappedStatement);
                // 查询总行数,如果当前PageResult对象包含总页数,则这里不做查询
                if (!pr.isHasTotalRecords()) {
                    int count = this.countRecords(connection, sql, mappedStatement, boundSql);
                    pr.setTotalRecords(count);
                }
                // 计算页数
                if (pr.getTotalRecords() > 0) {
                    int totalPages = pr.getTotalRecords() / pr.getPageSize()
                            + ((pr.getTotalRecords() % pr.getPageSize() > 0) ? 1 : 0);
                    pr.setTotalPages(totalPages); // 设置总页数
                }
                // 生成分页SQL
                final int number2 = 2;
                String pageSql = generatePageSql(sql, pr);
                invocation.getArgs()[number2] = RowBounds.DEFAULT;
                MappedStatement newMappedStatement = InterceptorUtil.copyFromNewSql(mappedStatement, boundSql, pageSql,
                        boundSql.getParameterMappings(), boundSql.getParameterObject());
                invocation.getArgs()[0] = newMappedStatement;
                // 执行分页查询
                Object result = invocation.proceed();
                // 得到处理结果
                pr.addAll((List) result);
                // 分页返回值
                return pr;
            }
            // 常规返回值
            return invocation.proceed();
        } finally {
            // 清理本地变量
            clearLocalPage();
        }
    }

    /**
     * 获得数据库连接(使用DynamicDataSource需重写此方法)
     *
     * @param mappedStatement mappedStatement
     * @return 数据库连接
     * @throws SQLException sql异常
     */
    protected Connection getConnection(MappedStatement mappedStatement) throws SQLException {
        DataSource dataSource = mappedStatement.getConfiguration().getEnvironment().getDataSource();
        return dataSource.getConnection();
    }

    /**
     * 获得总记录数
     *
     * @param connection      connection
     * @param originalSql     originalSql
     * @param mappedStatement mappedStatement
     * @param boundSql        boundSql
     * @return 总记录数
     * @throws SQLException 异常
     */
    private int countRecords(Connection connection, String originalSql, MappedStatement mappedStatement,
                             BoundSql boundSql) throws SQLException {
        SqlParser sqlParser;
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            int totalRecorld = 0;
            sqlParser = new SqlParser();
            Object paramObject = boundSql.getParameterObject();
            String countSql = sqlParser.getSmartCountSql(originalSql);
            ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, paramObject, boundSql);
            countStmt = connection.prepareStatement(countSql);
            parameterHandler.setParameters(countStmt);
            rs = countStmt.executeQuery();
            if (rs.next()) {
                totalRecorld = rs.getInt(1);
            }
            return totalRecorld;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("rs.close():", e);
            }
            try {
                if (countStmt != null) {
                    countStmt.close();
                }
            } catch (SQLException e) {
                log.error("countStmt.close():", e);
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("connection.close():", e);
            }
        }
    }

    /**
     * 生成分页SQL语句
     *
     * @param sql sql语句
     * @param pr  分页信息
     * @return 分页sql语句
     */
    private String generatePageSql(String sql, PageResult pr) {
        if (pr != null && (this.myBatisProperties.getDialect() != null && !"".equals(this.myBatisProperties.getDialect()))) {
            StringBuilder pageSql = new StringBuilder();
            if ("mysql".equals(this.myBatisProperties.getDialect())) {
                pageSql.append(sql).append(" LIMIT " + (pr.getCurPage() - 1) * pr.getPageSize() + "," + pr.getPageSize());
            } else if ("oracle".equals(this.myBatisProperties.getDialect())) {
                pageSql.append(" SELECT * FROM (SELECT TMP_TB.*,ROWNUM ROW_ID FROM ( ");
                pageSql.append(sql);
                pageSql.append(" )  TMP_TB WHERE ROWNUM <= ");
                pageSql.append(pr.getPageSize() + ((pr.getCurPage() - 1) * pr.getPageSize()));
                pageSql.append(" ) WHERE ROW_ID >= ");
                pageSql.append(1 + ((pr.getCurPage() - 1) * pr.getPageSize()));
            }
            return pageSql.toString();
        } else {
            return sql;
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) { //NOSONAR
    }

}
