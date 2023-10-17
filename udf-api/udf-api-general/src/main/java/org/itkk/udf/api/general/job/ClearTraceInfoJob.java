package org.itkk.udf.api.general.job;

import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.api.common.service.TraceInfoService;
import org.itkk.udf.starter.cache.db.service.DbCacheService;
import org.itkk.udf.starter.core.CoreUtil;
import org.itkk.udf.starter.core.job.IJobHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClearTraceInfoJob implements IJobHandle {

    /**
     * dbCacheService
     */
    @Autowired
    private DbCacheService dbCacheService;

    /**
     * traceInfoService
     */
    @Autowired
    private TraceInfoService traceInfoService;

    @Override
    @Scheduled(cron = "0 0 1 * * ?")
    public void execution() {
        //开始时间
        long startTime = System.currentTimeMillis();
        //定义key
        final String key = "job:clearTraceInfo";
        //定义过期时间(10分钟)
        final long expire = 10 * 60 * 1000L;
        //定义job的traceId
        final String traceId = CoreUtil.getTraceId();
        //悲观锁-锁定任务
        if (dbCacheService.setNx(key, Long.valueOf(System.currentTimeMillis()).toString(), expire)) {
            //日志输出
            log.info("{} 清理追踪日志(lock success)", traceId);
            //开始处理
            try {
                traceInfoService.clearTraceInfo();
            } finally {
                //删除key
                dbCacheService.delete(key);
                //日志输出
                log.info("{} 清理追踪日志(unlock success)(耗时:{}毫秒)", traceId, (System.currentTimeMillis() - startTime));
            }
        } else {
            //日志输出
            log.info("{} 清理追踪日志(lock fail)", traceId);
        }
    }
}
