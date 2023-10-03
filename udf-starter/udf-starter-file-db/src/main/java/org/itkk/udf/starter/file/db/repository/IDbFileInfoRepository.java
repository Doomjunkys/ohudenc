package org.itkk.udf.starter.file.db.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.itkk.udf.starter.file.db.entity.DbFileInfoEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDbFileInfoRepository extends BaseMapper<DbFileInfoEntity> {

}
