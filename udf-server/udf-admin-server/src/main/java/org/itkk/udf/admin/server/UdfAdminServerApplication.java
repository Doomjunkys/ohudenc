/**
 * UdfEurekaServerDemoApplication.java
 * Created at 2016-10-02
 * Created by wangkang
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.admin.server;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.itkk.udf.core.BaseApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 描述 : 系统入口
 *
 * @author wangkang
 */
@SpringCloudApplication
@EnableAdminServer
@EnableTurbine
public class UdfAdminServerApplication extends BaseApplication {

    /**
     * 描述 : spring boot的入口，在整个项目中，包括其子项目在内， 只能有一个main方法，否则spring boot启动不起来
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        SpringApplication.run(UdfAdminServerApplication.class, args);
    }

    /**
     * 描述 : SecurityConfig
     *
     * @author Administrator
     */
    @Configuration
    public static class SecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // Page with login form is served as /login.html and does a POST on /login
            http.formLogin().loginPage("/login.html").loginProcessingUrl("/login").permitAll();
            // The UI does a POST on /logout on logout
            http.logout().logoutUrl("/logout");
            // The ui currently doesn't support csrf
            http.csrf().disable();
            // Requests for the login page and the static assets are allowed
            http.authorizeRequests().antMatchers("/login.html", "/**/*.css", "/img/**", "/third-party/**")
                    .permitAll();
            // ... and any other request needs to be authorized
            http.authorizeRequests().antMatchers("/**").authenticated();
            // Enable so that the clients can authenticate via HTTP basic for registering
            http.httpBasic();
        }
    }

}
