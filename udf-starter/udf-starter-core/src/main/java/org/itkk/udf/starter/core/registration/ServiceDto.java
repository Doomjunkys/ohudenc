package org.itkk.udf.starter.core.registration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(callSuper = false)
public class ServiceDto implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6660352674554200657L; //ID
    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * ip
     */
    private String ip;

    /**
     * port
     */
    private String port;

    /**
     * managementPort
     */
    private String managementPort;
}
