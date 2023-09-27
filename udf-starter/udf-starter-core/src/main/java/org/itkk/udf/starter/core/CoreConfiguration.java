package org.itkk.udf.starter.core;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.starter.core.datasource.DruidBuild;
import org.itkk.udf.starter.core.exception.handle.RmsResponseErrorHandler;
import org.itkk.udf.starter.core.id.IdWorker;
import org.itkk.udf.starter.core.trace.TraceContentCachingFilter;
import org.itkk.udf.starter.core.xss.XssFilter;
import org.itkk.udf.starter.core.xss.XssStringJsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableAsync
@EnableRetry
@EnableScheduling
@Configuration
@Slf4j
public class CoreConfiguration {

    /**
     * coreProperties
     */
    @Autowired
    private CoreProperties coreProperties;

    /**
     * mybatisPlus定制属性
     */
    @Bean
    public List<ConfigurationCustomizer> configurationCustomizersProvider() {
        return Lists.newArrayList(configuration -> configuration.setObjectWrapperFactory(new MybatisMapWrapperFactory()));
    }

    /**
     * 默认Guava缓存
     *
     * @return
     */
    @Bean
    @Primary
    public Cache<String, Object> defGuavaCache() {
        //构造
        return CacheBuilder.newBuilder()
                //设置并发级别，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(coreProperties.getGuavaCacheProperties().getConcurrencyLevel())
                //设置缓存容器的初始容量
                .initialCapacity(coreProperties.getGuavaCacheProperties().getInitialCapacity())
                //设置缓存最大容量，超过容量之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(coreProperties.getGuavaCacheProperties().getMaximumSize())
                //设置写缓存后n分钟过期
                .expireAfterWrite(coreProperties.getGuavaCacheProperties().getConcurrencyLevel(), TimeUnit.MINUTES)
                //缓存的移除通知
                //.removalListener(notification -> log.info("defGuavaCache缓存移除,key : {} , cause : {} ", notification.getKey(), notification.getCause()))
                //构造
                .build();
    }

    /**
     * internalObjectMapper
     *
     * @param builder
     * @return
     */
    @Bean("internalObjectMapper")
    public ObjectMapper internalObjectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder.createXmlMapper(false).build();
    }

    /**
     * xssObjectMapper
     *
     * @param builder builder
     * @return ObjectMapper
     */
    @Bean
    @Primary
    public ObjectMapper xssObjectMapper(Jackson2ObjectMapperBuilder builder) {
        //解析器
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        // 注册xss解析器
        SimpleModule xssModule = new SimpleModule("XssStringJsonSerializer");
        xssModule.addSerializer(new XssStringJsonSerializer());
        objectMapper.registerModule(xssModule);
        // 返回
        return objectMapper;
    }

    /**
     * druidBuild
     *
     * @return DruidBuild
     */
    @Bean
    public DruidBuild druidBuild() {
        return new DruidBuild();
    }

    /**
     * idWorker
     *
     * @return IdWorker
     */
    @Bean
    public IdWorker idWorker() {
        return new IdWorker();
    }

    /**
     * 分页插件
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        List<ISqlParser> sqlParserList = new ArrayList<>();
        // 攻击 SQL 阻断解析器、加入解析链
        sqlParserList.add(new BlockAttackSqlParser());
        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }

    /**
     * traceContentCachingFilter
     *
     * @return TraceContentCachingFilter
     */
    @Bean
    public TraceContentCachingFilter traceContentCachingFilter() {
        return new TraceContentCachingFilter();
    }

    /**
     * xssFilter
     *
     * @return XssFilter
     */
    @Bean
    public XssFilter xssFilter() {
        return new XssFilter();
    }

    /**
     * xssFilterRegistry
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean xssFilterRegistry() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(xssFilter());
        registration.addUrlPatterns(coreProperties.getXssFilterPathPattern());
        registration.setName("xssFilter");
        registration.setOrder(Integer.MIN_VALUE);
        return registration;
    }

    /**
     * traceContentCachingFilterRegistry
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean traceContentCachingFilterRegistry() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(traceContentCachingFilter());
        registration.addUrlPatterns(coreProperties.getTraceFilterPathPattern());
        registration.setName("traceContentCachingFilter");
        registration.setOrder(Integer.MIN_VALUE + 1);
        return registration;
    }

    /**
     * restTemplate
     *
     * @param requestFactory requestFactory
     * @return RestTemplate
     */
    @Bean
    @Primary
    public RestTemplate restTemplate(ClientHttpRequestFactory requestFactory) {
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RmsResponseErrorHandler());
        return restTemplate;
    }

    /**
     * requestFactory
     *
     * @return ClientHttpRequestFactory
     */
    @Bean
    public ClientHttpRequestFactory requestFactory() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(coreProperties.getHttpConnectTimeout());
        //requestFactory.setReadTimeout(coreProperties.getHttpReadTimeout());
        requestFactory.setBufferRequestBody(coreProperties.getBufferRequestBody());
        return requestFactory;
    }

    /**
     * 跨域配置
     *
     * @return WebMvcConfigurer
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(coreProperties.getCorsPathPattern())
                        .allowedOrigins(coreProperties.getCorsAllowedOrigins().split(","))
                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
                        .allowedHeaders("*")
                        .exposedHeaders("traceId");
            }
        };
    }

}
