package org.itkk.udf.starter.cache.db.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.itkk.udf.starter.cache.db.entity.DbCacheEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDbCacheRepository extends BaseMapper<DbCacheEntity> {
    /**
     * 删除过期的缓存
     */
    void clearExpireCahce();
}
