package org.itkk.udf.api.common.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.itkk.udf.api.common.entity.DataHisEntity;

@Mapper
public interface IDataHisRepository extends BaseMapper<DataHisEntity> {
}
