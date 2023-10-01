package org.itkk.udf.starter.cache.db.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.itkk.udf.starter.cache.db.dto.DbCacheDto;
import org.itkk.udf.starter.cache.db.entity.DbCacheEntity;
import org.itkk.udf.starter.cache.db.repository.IDbCacheRepository;
import org.itkk.udf.starter.core.CoreUtil;
import org.itkk.udf.starter.core.exception.ParameterValidException;
import org.itkk.udf.starter.core.lock.FileLockWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class DbCacheService {

    /**
     * key最大长度
     */
    private static final long MAX_KEY_LENGTH = 1000;

    /**
     * 环境变量
     */
    @Autowired
    private Environment env;

    /**
     * iDbCacheRepository
     */
    @Autowired
    private IDbCacheRepository iDbCacheRepository;

    /**
     * 设置ttl
     *
     * @param key    key
     * @param expire expire
     * @param wait   wait
     */
    public void setTtl(String key, Long expire, boolean wait) {
        final String lockName = getKey(key) + ".lock";
        try (FileLockWrapper fileLockWrapper = new FileLockWrapper(lockName, wait)) {
            if (fileLockWrapper.isValid()) {
                this.setTtl(key, expire);
            }
        }
    }

    /**
     * 设置ttl
     *
     * @param key    key
     * @param expire expire
     */
    public void setTtl(String key, Long expire) {
        //构造key
        key = getKey(key);
        //获得缓存实体
        DbCacheEntity entity = getEntity(key);
        //判空
        if (entity != null) {
            entity = new DbCacheEntity();
            entity.setKey(key);
            entity.setCreateTime(System.currentTimeMillis());
            entity.setExpire(expire);
            entity.setExpireTime(entity.getCreateTime() + entity.getExpire());
            entity.setUpdateDate(new Date());
            iDbCacheRepository.updateById(entity);
        }
    }

    /**
     * 设置缓存,成功返回true,失败返回false
     *
     * @param key    key
     * @param value  value
     * @param expire expire
     * @param wait   wait
     * @return boolean
     */
    public boolean setNx(String key, String value, Long expire, boolean wait) {
        final String lockName = getKey(key) + ".lock";
        try (FileLockWrapper fileLockWrapper = new FileLockWrapper(lockName, wait)) {
            if (fileLockWrapper.isValid()) {
                return setNx(key, value, expire);
            } else {
                return false;
            }
        }
    }

    /**
     * 设置缓存,成功返回true,失败返回false
     *
     * @param key    key
     * @param value  value
     * @param expire expire
     * @return boolean
     */
    public boolean setNx(String key, String value, Long expire) {
        //构造key
        key = getKey(key);
        //返回值
        boolean setSuccess = false;
        //参数检查
        setCheck(key, value, expire);
        //获得缓存实体
        DbCacheEntity entity = getEntity(key);
        //判空
        if (entity == null) {
            //新增缓存
            entity = new DbCacheEntity();
            entity.setKey(key);
            entity.setValue(value);
            entity.setCreateTime(System.currentTimeMillis());
            entity.setExpire(expire);
            entity.setExpireTime(entity.getCreateTime() + entity.getExpire());
            entity.setCreateDate(new Date());
            entity.setUpdateDate(new Date());
            try {
                iDbCacheRepository.insert(entity);
                setSuccess = true;
            } catch (Exception e) {
                log.warn("{} setNx失败 , key : {}", CoreUtil.getTraceId(), key);
            }
        }
        //返回
        return setSuccess;
    }

    /**
     * 设置缓存
     *
     * @param key    key
     * @param value  value
     * @param expire expire
     * @param wait   wait
     */
    public void set(String key, String value, Long expire, boolean wait) {
        final String lockName = getKey(key) + ".lock";
        try (FileLockWrapper fileLockWrapper = new FileLockWrapper(lockName, wait)) {
            if (fileLockWrapper.isValid()) {
                set(key, value, expire);
            }
        }
    }

    /**
     * 设置缓存
     *
     * @param key    key
     * @param value  value
     * @param expire expire
     */
    public void set(String key, String value, Long expire) {
        //构造key
        key = getKey(key);
        //参数检查
        setCheck(key, value, expire);
        //获得缓存实体
        DbCacheEntity entity = getEntity(key);
        //判空
        if (entity == null) {
            //新增缓存
            entity = new DbCacheEntity();
            entity.setKey(key);
            entity.setValue(value);
            entity.setCreateTime(System.currentTimeMillis());
            entity.setExpire(expire);
            entity.setExpireTime(entity.getCreateTime() + entity.getExpire());
            entity.setCreateDate(new Date());
            entity.setUpdateDate(new Date());
            iDbCacheRepository.insert(entity);
        } else {
            //更新缓存
            entity.setValue(value);
            entity.setCreateTime(System.currentTimeMillis());
            entity.setExpire(expire);
            entity.setExpireTime(entity.getCreateTime() + entity.getExpire());
            entity.setUpdateDate(new Date());
            iDbCacheRepository.updateById(entity);
        }
    }

    /**
     * 删除缓存
     *
     * @param key key
     */
    public void delete(String key) {
        //构造key
        key = getKey(key);
        //删除
        iDbCacheRepository.deleteById(key);
    }

    /**
     * 删除缓存
     *
     * @param keyPrefix keyPrefix
     */
    public void deleteAll(String keyPrefix) {
        //构造key
        keyPrefix = getKey(keyPrefix) + "%";
        //删除
        iDbCacheRepository.delete(new QueryWrapper<DbCacheEntity>().lambda().like(DbCacheEntity::getKey, keyPrefix));
    }

    /**
     * DbCacheDtos
     *
     * @param keyPrefix keyPrefix
     * @return List<DbCacheDto>
     */
    public List<DbCacheDto> getDtos(String keyPrefix) {
        //定义返回值
        List<DbCacheDto> rv = null;
        //构造key
        keyPrefix = getKey(keyPrefix) + "%";
        //获得key清单
        List<DbCacheEntity> dbCacheEntityList = iDbCacheRepository.selectList(new QueryWrapper<DbCacheEntity>().lambda().select(DbCacheEntity::getKey).like(DbCacheEntity::getKey, keyPrefix));
        //判空
        if (CollectionUtils.isNotEmpty(dbCacheEntityList)) {
            //初始化
            rv = new ArrayList<>();
            //遍历
            for (DbCacheEntity dbCacheEntity : dbCacheEntityList) {
                //获得缓存实体
                DbCacheEntity entity = getEntity(dbCacheEntity.getKey());
                //判空
                if (entity != null) {
                    //放入结果集
                    rv.add(new DbCacheDto().setValue(entity.getValue()).setExpireTime(entity.getExpireTime()));
                }
            }
        }
        //返回
        return rv;
    }

    /**
     * DbCacheDto
     *
     * @param key key
     * @return DbCacheDto
     */
    public DbCacheDto getDto(String key) {
        //构造key
        key = getKey(key);
        //获得缓存实体
        DbCacheEntity entity = getEntity(key);
        //返回
        return entity == null ? null : new DbCacheDto().setValue(entity.getValue()).setExpireTime(entity.getExpireTime());
    }

    /**
     * 获得缓存(根据前缀)
     *
     * @param keyPrefix keyPrefix
     * @return List<String>
     */
    public List<String> gets(String keyPrefix) {
        //定义返回值
        List<String> rv = null;
        //构造key
        keyPrefix = getKey(keyPrefix) + "%";
        //获得key清单
        List<DbCacheEntity> dbCacheEntityList = iDbCacheRepository.selectList(new QueryWrapper<DbCacheEntity>().lambda().select(DbCacheEntity::getKey).like(DbCacheEntity::getKey, keyPrefix));
        //判空
        if (CollectionUtils.isNotEmpty(dbCacheEntityList)) {
            //初始化
            rv = new ArrayList<>();
            //遍历
            for (DbCacheEntity dbCacheEntity : dbCacheEntityList) {
                //获得缓存实体
                DbCacheEntity entity = getEntity(dbCacheEntity.getKey());
                //判空
                if (entity != null) {
                    //放入结果集
                    rv.add(entity.getValue());
                }
            }
        }
        //返回
        return rv;
    }

    /**
     * 获得缓存
     *
     * @param key key
     * @return String
     */
    public String get(String key) {
        //构造key
        key = getKey(key);
        //获得缓存实体
        DbCacheEntity entity = getEntity(key);
        //返回
        return entity == null ? null : entity.getValue();
    }

    /**
     * 清理过期的缓存
     */
    public void clearExpireCahce() {
        iDbCacheRepository.clearExpireCahce();
    }

    /**
     * set 参数检查
     *
     * @param key    key
     * @param value  value
     * @param expire expire
     */
    private void setCheck(String key, String value, Long expire) {
        //参数校验
        if (StringUtils.isBlank(key) || key.length() > MAX_KEY_LENGTH) {
            throw new ParameterValidException("key不能为空,并且长度不能大于" + MAX_KEY_LENGTH);
        }
        if (StringUtils.isBlank(value)) {
            throw new ParameterValidException("value不能为空");
        }
        if (expire == null || 0 > expire) {
            throw new ParameterValidException("expire不能为空,并且不能小于0");
        }
    }

    /**
     * 获得缓存实体
     *
     * @param key key
     * @return DbCacheEntity
     */
    private DbCacheEntity getEntity(String key) {
        //获得当前key
        DbCacheEntity dbCacheEntity = iDbCacheRepository.selectById(key);
        //判空
        if (dbCacheEntity == null) {
            return null;
        }
        //判断是否过期,如果已经过期,则删除key,并且返回null
        long cacheExpireTime = dbCacheEntity.getCreateTime() + dbCacheEntity.getExpire();
        if (System.currentTimeMillis() > cacheExpireTime) {
            iDbCacheRepository.deleteById(key);
            return null;
        }
        //返回
        return dbCacheEntity;
    }

    /**
     * 获得key(拼接上前缀的key)
     *
     * @param key key
     * @return String
     */
    private String getKey(String key) {
        return keyPrefix() + key;
    }

    /**
     * 获得key前缀
     *
     * @return String
     */
    private String keyPrefix() {
        String profilesActive = env.getProperty("spring.profiles.active");
        String applicationName = env.getProperty("spring.application.name");
        return applicationName + ":" + profilesActive + ":";
    }
}
