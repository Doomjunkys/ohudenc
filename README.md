# UDF

基于spring boot / spring cloud 的基础项目,脚手架,主要用于学习和实践

按照spring boot的思想,将各个不同的功能按照starter的形式拆分开来,做到灵活组合

## 物理架构示意

![物理架构示意](https://static.oschina.net/uploads/img/201708/25093208_nNaX.png "物理架构示意")

## 逻辑架构示意

![逻辑架构示意](https://static.oschina.net/uploads/img/201708/25093422_TzrG.png "逻辑架构示意")

## CI & CD 示意

![CI & CD 示意](https://static.oschina.net/uploads/img/201708/25093522_sKFz.png "CI & CD 示意")

## 代码仓库 (点击跳转)

- [udf-starter : 基础项目,脚手架,框架](https://gitee.com/wangkang/udf)

- [udf-sample : 集成样例](https://gitee.com/wangkang/udf-sample)

## 项目博客 (点击跳转)

- [spring boot / cloud (十二) 异常统一处理进阶](https://my.oschina.net/wangkang80/blog/1519189)

- [spring boot / cloud (十一) 回归-博客配套项目开源](https://my.oschina.net/wangkang80/blog/1517938)

- [spring boot / cloud (十) 使用quartz搭建调度中心](https://my.oschina.net/wangkang80/blog/983208)

- [spring boot / cloud (十) 使用quartz搭建调度中心](https://my.oschina.net/wangkang80/blog/983208)

- [spring boot / cloud (九) 使用rabbitmq消息中间件](https://my.oschina.net/wangkang80/blog/955328)

- [spring boot / cloud (八) 使用RestTemplate来构建远程调用服务](https://my.oschina.net/wangkang80/blog/919955)

- [spring boot / cloud (七) 使用@Retryable来进行重处理](https://my.oschina.net/wangkang80/blog/912941)

- [spring boot / cloud (六) 开启CORS跨域访问](https://my.oschina.net/wangkang80/blog/912270)

- [spring boot / cloud (五) 自签SSL证书以及HTTPS](https://my.oschina.net/wangkang80/blog/911484)

- [spring boot / cloud (四) 自定义线程池以及异步处理@Async](https://my.oschina.net/wangkang80/blog/910041)

- [spring boot / cloud (三) 集成springfox-swagger2构建在线API文档](https://my.oschina.net/wangkang80/blog/909448)

- [spring boot / cloud (二) 规范响应格式以及统一异常处理](https://my.oschina.net/wangkang80/blog/908919)

- [spring boot / cloud (一) 使用filter防止XSS](https://my.oschina.net/wangkang80/blog/908070)

## 项目包说明

### udf-starter

- udf-start-core : 核心包,包含统一异常处理,标准json输出,在线api文档,线程池配置,xss处理,等

- udf-starter-cors : 跨域请求配置

- udf-starter-file : 文件服务

- udf-starter-id : 分布式ID服务

- udf-starter-mail : 邮件服务

- udf-starter-qrcode : 二维码服务

- udf-starter-rms : rest远程调用服务,包含服务间认证和治理

- udf-starter-scheduler : 分布式调度服务

- udf-starter-scheduler-client : 分布式调度客户端

- udf-starter-datasource : 数据源

- udf-starter-connection-pool-druid : druid数据源

- udf-starter-amqp-rabbitmq : 消息中间件

- udf-starter-auth : 用户认证服务

- udf-starter-datasource-dynamic : 动态数据源
        
### udf-sample

- udf-eureka-config-server-demo  : 服务注册中心 和 配置中心

- udf-admin-server-demo : 监控中心 和 断路器看板

- udf-config-hub : 配置文件仓库

- udf-zuul-server-demo : 网关

- udf-general-server-demo : 公共服务

- udf-service-a-demo : demo服务
    
    
## csdn博客地址 (博客同步更新)

http://blog.csdn.net/wangkang80/    

## 公众号二维码 (博客同步更新,并主动推送)

想获得最快更新,请关注公众号

![想获得最快更新,请关注公众号](https://static.oschina.net/uploads/img/201705/24155414_Pukg.jpg "想获得最快更新,请关注公众号") 

