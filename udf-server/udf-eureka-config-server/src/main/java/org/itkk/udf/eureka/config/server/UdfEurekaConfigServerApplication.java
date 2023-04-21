package org.itkk.udf.eureka.config.server;

import org.itkk.udf.core.BaseApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 描述 : 系统入口
 *
 * @author wangkang
 */
@SpringCloudApplication
@EnableEurekaServer
@EnableConfigServer
public class UdfEurekaConfigServerApplication extends BaseApplication {

    /**
     * 描述 : spring boot的入口，在整个项目中，包括其子项目在内， 只能有一个main方法，否则spring boot启动不起来
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        SpringApplication.run(UdfEurekaConfigServerApplication.class, args);
    }

}
