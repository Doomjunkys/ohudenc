package org.itkk.udf.starter.cache.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.itkk.udf.starter.cache.db.service.DbCacheService;
import org.itkk.udf.starter.core.CoreConstant;
import org.itkk.udf.starter.core.registration.IServiceRegistration;
import org.itkk.udf.starter.core.registration.ServiceDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DbCacheServiceRegistration implements IServiceRegistration {

    /**
     * 缓存前缀
     */
    private static final String KEY_PREFIX = "SERVICE_REGISTRATION:";

    /**
     * 过期时间140秒
     */
    private static final long EXPIRE = 140 * 1000L;

    /**
     * cron
     */
    private static final String CRON = "0 0/2 * * * ? ";

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * ip
     */
    private String ip;

    /**
     * port
     */
    private String port;

    /**
     * managementPort
     */
    private String managementPort;

    /**
     * env
     */
    @Autowired
    private Environment env;

    /**
     * dbCacheService
     */
    @Autowired
    private DbCacheService dbCacheService;

    /**
     * internalObjectMapper
     */
    @Autowired
    @Qualifier("internalObjectMapper")
    private ObjectMapper internalObjectMapper;

    /**
     * 获得服务注册service
     *
     * @return IServiceRegistration
     */
    public static IServiceRegistration get() {
        return CoreConstant.applicationContext.getBean(DbCacheServiceRegistration.class);
    }

    /**
     * 初始化
     *
     * @throws UnknownHostException    UnknownHostException
     * @throws JsonProcessingException JsonProcessingException
     */
    @PostConstruct
    public void init() throws UnknownHostException, JsonProcessingException {
        //获得IP和端口
        this.applicationName = env.getProperty("spring.application.name");
        this.ip = InetAddress.getLocalHost().getHostAddress();
        this.port = env.getProperty("server.port");
        this.managementPort = env.getProperty("management.server.port");
        //放入缓存
        dbCacheService.set(getKey(), internalObjectMapper.writeValueAsString(new ServiceDto().setApplicationName(this.applicationName).setIp(this.ip).setPort(this.port).setManagementPort(this.managementPort)), DbCacheServiceRegistration.EXPIRE);
        log.info("服务上线:{}", getKey());
    }

    /**
     * 关闭
     */
    @PreDestroy
    public void shutdown() {
        //删除缓存
        dbCacheService.delete(getKey());
        log.info("服务下线:{}", getKey());
    }

    /**
     * 定时刷新
     *
     * @throws JsonProcessingException JsonProcessingException
     */
    @Scheduled(cron = DbCacheServiceRegistration.CRON)
    public void refresh() throws JsonProcessingException {
        //更新缓存
        dbCacheService.set(getKey(), internalObjectMapper.writeValueAsString(new ServiceDto().setApplicationName(this.applicationName).setIp(this.ip).setPort(this.port).setManagementPort(this.managementPort)), DbCacheServiceRegistration.EXPIRE);
        log.info("续订服务状态:{}", getKey());
    }

    @Override
    public ServiceDto loacl() {
        return new ServiceDto().setApplicationName(this.applicationName).setIp(this.ip).setPort(this.port).setManagementPort(this.managementPort);
    }

    @Override
    public boolean isLocal(ServiceDto serviceDto) {
        return serviceDto.getApplicationName().equals(this.applicationName) && serviceDto.getIp().equals(this.ip) && serviceDto.getPort().equals(this.port);
    }

    @Override
    public List<ServiceDto> list() {
        return this.list(dbCacheService.gets(DbCacheServiceRegistration.KEY_PREFIX));
    }

    @Override
    public List<ServiceDto> list(String serviceName) {
        return this.list(dbCacheService.gets(DbCacheServiceRegistration.KEY_PREFIX + serviceName));
    }

    /**
     * 获得服务清单
     *
     * @param caches caches
     * @return List<ServiceDto>
     */
    private List<ServiceDto> list(List<String> caches) {
        //定义返回值
        List<ServiceDto> rv = null;
        //判空
        if (CollectionUtils.isNotEmpty(caches)) {
            //实例化
            rv = new ArrayList<>();
            //遍历
            for (String cache : caches) {
                try {
                    rv.add(internalObjectMapper.readValue(cache, ServiceDto.class));
                } catch (JsonProcessingException e) {
                    log.warn("服务清单转换失败.", e);
                }
            }
        }
        //返回
        return rv;
    }

    /**
     * 获得key
     *
     * @return String
     */
    private String getKey() {
        return DbCacheServiceRegistration.KEY_PREFIX + this.applicationName + ":" + this.ip + ":" + this.port;
    }

}
