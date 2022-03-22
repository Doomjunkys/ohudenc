package org.itkk.udf.core.domain.datasource;

/**
 * 描述 : 数据源属性
 *
 * @author wangkang
 */
public class DataSourceMeta {

    /**
     * 描述 : 数据源代码(唯一)
     */
    private String code;

    /**
     * 描述 : 是否启用(默认启用)
     */
    private Boolean enable = true;

    /**
     * 描述 : 数据库连接url
     */
    private String url;

    /**
     * 描述 : 数据库账号
     */
    private String username;

    /**
     * 描述 : 数据库密码
     */
    private String password;

    /**
     * 描述 : 数据库驱动类名
     */
    private String driverClassName;

    /**
     * 描述 : 连接池初始容量
     */
    private Integer initialSize;

    /**
     * 描述 : 连接池最小容量
     */
    private Integer minIdle;

    /**
     * 描述 : 连接池最大容量
     */
    private Integer maxActive;

    /**
     * 描述 : 获取连接等待超时的时间
     */
    private Long maxWait;

    /**
     * 描述 : 间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
     */
    private Long timeBetweenEvictionRunsMillis;

    /**
     * 描述 : 一个连接在池中最小生存的时间，单位是毫秒
     */
    private Long minEvictableIdleTimeMillis;

    /**
     * 描述 : 用来检测连接是否有效的sql，要求是一个查询语句
     */
    private String validationQuery;

    /**
     * 描述 : 申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效
     */
    private Boolean testWhileIdle;

    /**
     * 描述 : 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
     */
    private Boolean testOnBorrow;

    /**
     * 描述 : 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
     */
    private Boolean testOnReturn;

    /**
     * 描述 : 是否缓存preparedStatement
     */
    private Boolean poolPreparedStatements;

    /**
     * 描述 : 要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true
     */
    private Integer maxPoolPreparedStatementPerConnectionSize;

    /**
     * 描述 : 属性类型是字符串，通过别名的方式配置扩展插件
     */
    private String filters;

    /**
     * 描述 : 通过connectProperties属性来打开mergeSql功能；慢SQL记录
     */
    private String connectionProperties;

    /**
     * 描述 : 合并多个DruidDataSource的监控数据
     */
    private Boolean useGlobalDataSourceStat;

    /**
     * 描述 : 获取enable
     *
     * @return the enable
     */
    public Boolean getEnable() {
        return enable;
    }

    /**
     * 描述 : 设置enable
     *
     * @param enable the enable to set
     */
    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    /**
     * 描述 : 获取code
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * 描述 : 设置code
     *
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 描述 : 获取url
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 描述 : 设置url
     *
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 描述 : 获取username
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 描述 : 设置username
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 描述 : 获取password
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 描述 : 设置password
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 描述 : 获取driverClassName
     *
     * @return the driverClassName
     */
    public String getDriverClassName() {
        return driverClassName;
    }

    /**
     * 描述 : 设置driverClassName
     *
     * @param driverClassName the driverClassName to set
     */
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    /**
     * 描述 : 获取initialSize
     *
     * @return the initialSize
     */
    public Integer getInitialSize() {
        return initialSize;
    }

    /**
     * 描述 : 设置initialSize
     *
     * @param initialSize the initialSize to set
     */
    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    /**
     * 描述 : 获取minIdle
     *
     * @return the minIdle
     */
    public Integer getMinIdle() {
        return minIdle;
    }

    /**
     * 描述 : 设置minIdle
     *
     * @param minIdle the minIdle to set
     */
    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    /**
     * 描述 : 获取maxActive
     *
     * @return the maxActive
     */
    public Integer getMaxActive() {
        return maxActive;
    }

    /**
     * 描述 : 设置maxActive
     *
     * @param maxActive the maxActive to set
     */
    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    /**
     * 描述 : 获取maxWait
     *
     * @return the maxWait
     */
    public Long getMaxWait() {
        return maxWait;
    }

    /**
     * 描述 : 设置maxWait
     *
     * @param maxWait the maxWait to set
     */
    public void setMaxWait(Long maxWait) {
        this.maxWait = maxWait;
    }

    /**
     * 描述 : 获取timeBetweenEvictionRunsMillis
     *
     * @return the timeBetweenEvictionRunsMillis
     */
    public Long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    /**
     * 描述 : 设置timeBetweenEvictionRunsMillis
     *
     * @param timeBetweenEvictionRunsMillis the timeBetweenEvictionRunsMillis to set
     */
    public void setTimeBetweenEvictionRunsMillis(Long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    /**
     * 描述 : 获取minEvictableIdleTimeMillis
     *
     * @return the minEvictableIdleTimeMillis
     */
    public Long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    /**
     * 描述 : 设置minEvictableIdleTimeMillis
     *
     * @param minEvictableIdleTimeMillis the minEvictableIdleTimeMillis to set
     */
    public void setMinEvictableIdleTimeMillis(Long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    /**
     * 描述 : 获取validationQuery
     *
     * @return the validationQuery
     */
    public String getValidationQuery() {
        return validationQuery;
    }

    /**
     * 描述 : 设置validationQuery
     *
     * @param validationQuery the validationQuery to set
     */
    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    /**
     * 描述 : 获取testWhileIdle
     *
     * @return the testWhileIdle
     */
    public Boolean getTestWhileIdle() {
        return testWhileIdle;
    }

    /**
     * 描述 : 设置testWhileIdle
     *
     * @param testWhileIdle the testWhileIdle to set
     */
    public void setTestWhileIdle(Boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    /**
     * 描述 : 获取testOnBorrow
     *
     * @return the testOnBorrow
     */
    public Boolean getTestOnBorrow() {
        return testOnBorrow;
    }

    /**
     * 描述 : 设置testOnBorrow
     *
     * @param testOnBorrow the testOnBorrow to set
     */
    public void setTestOnBorrow(Boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    /**
     * 描述 : 获取testOnReturn
     *
     * @return the testOnReturn
     */
    public Boolean getTestOnReturn() {
        return testOnReturn;
    }

    /**
     * 描述 : 设置testOnReturn
     *
     * @param testOnReturn the testOnReturn to set
     */
    public void setTestOnReturn(Boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    /**
     * 描述 : 获取poolPreparedStatements
     *
     * @return the poolPreparedStatements
     */
    public Boolean getPoolPreparedStatements() {
        return poolPreparedStatements;
    }

    /**
     * 描述 : 设置poolPreparedStatements
     *
     * @param poolPreparedStatements the poolPreparedStatements to set
     */
    public void setPoolPreparedStatements(Boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    /**
     * 描述 : 获取maxPoolPreparedStatementPerConnectionSize
     *
     * @return the maxPoolPreparedStatementPerConnectionSize
     */
    public Integer getMaxPoolPreparedStatementPerConnectionSize() {
        return maxPoolPreparedStatementPerConnectionSize;
    }

    /**
     * 描述 : 设置maxPoolPreparedStatementPerConnectionSize
     *
     * @param maxPoolPreparedStatementPerConnectionSize the maxPoolPreparedStatementPerConnectionSize to set
     */
    public void setMaxPoolPreparedStatementPerConnectionSize(Integer maxPoolPreparedStatementPerConnectionSize) {
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
    }

    /**
     * 描述 : 获取filters
     *
     * @return the filters
     */
    public String getFilters() {
        return filters;
    }

    /**
     * 描述 : 设置filters
     *
     * @param filters the filters to set
     */
    public void setFilters(String filters) {
        this.filters = filters;
    }

    /**
     * 描述 : 获取connectionProperties
     *
     * @return the connectionProperties
     */
    public String getConnectionProperties() {
        return connectionProperties;
    }

    /**
     * 描述 : 设置connectionProperties
     *
     * @param connectionProperties the connectionProperties to set
     */
    public void setConnectionProperties(String connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    /**
     * 描述 : 获取useGlobalDataSourceStat
     *
     * @return the useGlobalDataSourceStat
     */
    public Boolean getUseGlobalDataSourceStat() {
        return useGlobalDataSourceStat;
    }

    /**
     * 描述 : 设置useGlobalDataSourceStat
     *
     * @param useGlobalDataSourceStat the useGlobalDataSourceStat to set
     */
    public void setUseGlobalDataSourceStat(Boolean useGlobalDataSourceStat) {
        this.useGlobalDataSourceStat = useGlobalDataSourceStat;
    }

}
