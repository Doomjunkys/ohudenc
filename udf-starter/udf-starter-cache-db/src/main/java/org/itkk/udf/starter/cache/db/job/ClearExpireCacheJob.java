package org.itkk.udf.starter.cache.db.job;

import org.itkk.udf.starter.cache.db.service.DbCacheService;
import org.itkk.udf.starter.core.CoreUtil;
import org.itkk.udf.starter.core.job.IJobHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClearExpireCacheJob implements IJobHandle {
    /**
     * dbCacheService
     */
    @Autowired
    private DbCacheService dbCacheService;

    @Override
    @Scheduled(cron = "0 0/30 * * * ?")
    public void execution() {
        //开始时间
        long startTime = System.currentTimeMillis();
        //定义key
        final String key = "job:clearExpireCahce";
        //定义过期时间(五分钟)
        final long expire = 5 * 60 * 1000;
        //定义job的traceId
        final String traceId = CoreUtil.getTraceId();
        //悲观锁-锁定任务
        if (dbCacheService.setNx(key, Long.valueOf(System.currentTimeMillis()).toString(), expire)) {
            //日志输出
            log.info("{} 清理过期缓存(lock success)", traceId);
            //开始处理
            try {
                dbCacheService.clearExpireCahce();
            } finally {
                //删除key
                dbCacheService.delete(key);
                //日志输出
                log.info("{} 清理过期缓存(unlock success)(耗时:{}毫秒)", traceId, (System.currentTimeMillis() - startTime));
            }
        } else {
            //日志输出
            log.info("{} 清理过期缓存(lock fail)", traceId);
        }
    }
}
