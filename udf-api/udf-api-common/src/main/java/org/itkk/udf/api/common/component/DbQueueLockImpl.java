package org.itkk.udf.api.common.component;

import org.itkk.udf.starter.cache.db.service.DbCacheService;
import org.itkk.udf.starter.queue.db.IDbQueueLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DbQueueLockImpl implements IDbQueueLock {

    /**
     * dbCacheService
     */
    @Autowired
    private DbCacheService dbCacheService;

    @Override
    public boolean lock(String key) {
        final long expire = 30 * 60 * 1000L;
        return dbCacheService.setNx(key, key, expire);
    }

    @Override
    public void unlock(String key) {
        dbCacheService.delete(key);
    }
}
