/**
 * DruidStatViewConfig.java
 * Created at 2016-11-02
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package org.itkk.udf.connection.pool.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述 : DruidStatViewConfig
 *
 * @author wangkang
 */
@Configuration
@ConditionalOnProperty(value = "org.itkk.connection.pool.druid.minitor.enabled", matchIfMissing = true)
@ConfigurationProperties(prefix = "org.itkk.connection.pool.druid.minitor")
public class DruidMonitorConfig {

    /**
     * 描述 : 根路径
     */
    private String path;

    /**
     * 描述 : 白名单
     */
    private String allow;

    /**
     * 描述 : 黑名单
     */
    private String deny;

    /**
     * 描述 : 登入名
     */
    private String loginUsername;

    /**
     * 描述 : 密码
     */
    private String loginPassword;

    /**
     * 描述 : 是否能够重置数据
     */
    private String resetEnable;

    /**
     * 描述 : 过滤规则
     */
    private String urlPatterns;

    /**
     * 描述 : 添加不需要忽略的格式信息.
     */
    private String exclusions;

    /**
     * 注册一个StatViewServlet
     *
     * @return ServletRegistrationBean
     */
    @Bean
    public ServletRegistrationBean druidStatViewServle() {
        //org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),
                this.getPath());
        //白名单
        if (StringUtils.isNotEmpty(this.getAllow())) {
            servletRegistrationBean.addInitParameter("allow", this.getAllow());
        }
        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
        if (StringUtils.isNotEmpty(this.getDeny())) {
            servletRegistrationBean.addInitParameter("deny", this.getDeny());
        }
        //登入查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", this.getLoginUsername());
        servletRegistrationBean.addInitParameter("loginPassword", this.getLoginPassword());
        //是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", this.getResetEnable());
        return servletRegistrationBean;
    }

    /**
     * 注册一个：filterRegistrationBean
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean druidStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        //添加过滤规则.
        filterRegistrationBean.addUrlPatterns(this.getUrlPatterns());
        //添加不需要忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions", this.getExclusions());
        return filterRegistrationBean;
    }

    /**
     * 描述 : 获取path
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * 描述 : 设置path
     *
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 描述 : 获取allow
     *
     * @return the allow
     */
    public String getAllow() {
        return allow;
    }

    /**
     * 描述 : 设置allow
     *
     * @param allow the allow to set
     */
    public void setAllow(String allow) {
        this.allow = allow;
    }

    /**
     * 描述 : 获取deny
     *
     * @return the deny
     */
    public String getDeny() {
        return deny;
    }

    /**
     * 描述 : 设置deny
     *
     * @param deny the deny to set
     */
    public void setDeny(String deny) {
        this.deny = deny;
    }

    /**
     * 描述 : 获取loginUsername
     *
     * @return the loginUsername
     */
    public String getLoginUsername() {
        return loginUsername;
    }

    /**
     * 描述 : 设置loginUsername
     *
     * @param loginUsername the loginUsername to set
     */
    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    /**
     * 描述 : 获取loginPassword
     *
     * @return the loginPassword
     */
    public String getLoginPassword() {
        return loginPassword;
    }

    /**
     * 描述 : 设置loginPassword
     *
     * @param loginPassword the loginPassword to set
     */
    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    /**
     * 描述 : 获取resetEnable
     *
     * @return the resetEnable
     */
    public String getResetEnable() {
        return resetEnable;
    }

    /**
     * 描述 : 设置resetEnable
     *
     * @param resetEnable the resetEnable to set
     */
    public void setResetEnable(String resetEnable) {
        this.resetEnable = resetEnable;
    }

    /**
     * 描述 : 获取urlPatterns
     *
     * @return the urlPatterns
     */
    public String getUrlPatterns() {
        return urlPatterns;
    }

    /**
     * 描述 : 设置urlPatterns
     *
     * @param urlPatterns the urlPatterns to set
     */
    public void setUrlPatterns(String urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    /**
     * 描述 : 获取exclusions
     *
     * @return the exclusions
     */
    public String getExclusions() {
        return exclusions;
    }

    /**
     * 描述 : 设置exclusions
     *
     * @param exclusions the exclusions to set
     */
    public void setExclusions(String exclusions) {
        this.exclusions = exclusions;
    }

}
