package org.itkk.udf.api.server;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ServletComponentScan(basePackages = {"org.itkk"})
@ComponentScan(basePackages = {"org.itkk"})
@MapperScan(basePackages = {"org.itkk"}, annotationClass = Mapper.class)
@EnableAsync
public class UdfApiServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(UdfApiServerApplication.class, args);
    }
}
