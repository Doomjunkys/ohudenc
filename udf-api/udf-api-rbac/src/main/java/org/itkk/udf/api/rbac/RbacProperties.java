package org.itkk.udf.api.rbac;


import lombok.Data;
import org.itkk.udf.api.rbac.dto.RbacApiDto;
import org.itkk.udf.api.rbac.dto.RbacFunctionDto;
import org.itkk.udf.api.rbac.dto.RbacMenuDto;
import org.itkk.udf.api.rbac.dto.RbacRoleDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Component
@PropertySource(value = {
        "classpath:rbac/api.properties",
        "classpath:rbac/function.properties",
        "classpath:rbac/menu.properties",
        "classpath:rbac/role-public.properties",
        "classpath:rbac/role-ops.properties"
})
@ConfigurationProperties(prefix = RbacProperties.UDF_API_RBAC_PROPERTIES)
@Validated
@Data
public class RbacProperties {

    /**
     * 配置文件前缀
     */
    protected static final String UDF_API_RBAC_PROPERTIES = "org.itkk.udf.api.rbac";

    /**
     * 服务
     */
    @Valid
    private final Map<String, RbacApiDto> api;

    /**
     * 功能
     */
    @Valid
    private final Map<String, RbacFunctionDto> function;

    /**
     * 菜单
     */
    @Valid
    private final Map<String, RbacMenuDto> menu;

    /**
     * 角色 - 公共
     */
    @Valid
    private final Map<String, RbacRoleDto> rolePublic;

    /**
     * 运维人员
     */
    @Valid
    private final List<String> opsUsers;

    /**
     * 角色 - 运维
     */
    @Valid
    private final Map<String, RbacRoleDto> roleOps;

}
