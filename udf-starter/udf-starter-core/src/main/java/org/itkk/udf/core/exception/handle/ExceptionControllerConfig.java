package org.itkk.udf.core.exception.handle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;
import java.util.List;

/**
 * ErrorControllerConfig
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(ResourceProperties.class)
public class ExceptionControllerConfig {
    /**
     * errorViewResolvers
     */
    @Autowired(required = false)
    private List<ErrorViewResolver> errorViewResolvers;

    /**
     * serverProperties
     */
    private final ServerProperties serverProperties;

    /**
     * 构造函数
     *
     * @param serverProperties serverProperties
     */
    public ExceptionControllerConfig(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    /**
     * exceptionController
     *
     * @param errorAttributes errorAttributes
     * @return ExceptionController
     */
    @Bean
    public ExceptionController exceptionController(ErrorAttributes errorAttributes) {
        return new ExceptionController(errorAttributes, this.serverProperties.getError(), this.errorViewResolvers);
    }


}
