package org.itkk.udf.api.rbac.repository;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.itkk.udf.api.rbac.entity.RoleEntity;

@Mapper
public interface IRoleRepository extends BaseMapper<RoleEntity> {

}
