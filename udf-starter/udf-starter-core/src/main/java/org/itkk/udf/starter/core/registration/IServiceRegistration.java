package org.itkk.udf.starter.core.registration;

import java.util.List;

public interface IServiceRegistration {

    /**
     * 本地服务
     *
     * @return ServiceDto
     */
    ServiceDto loacl();

    /**
     * 是否本地
     *
     * @param serviceDto serviceDto
     * @return boolean
     */
    boolean isLocal(ServiceDto serviceDto);

    /**
     * 服务清单(所有)
     *
     * @return List<ServiceDto>
     */
    List<ServiceDto> list();

    /**
     * 服务清单(根据服务名称)
     *
     * @param serviceName serviceName
     * @return List<ServiceDto>
     */
    List<ServiceDto> list(String serviceName);
}
