package org.itkk.udf.starter.cache.db.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(callSuper = false)
public class DbCacheDto implements Serializable {
    /**
     * ID
     */
    private static final long serialVersionUID = -1369076998407328491L;
    /**
     * 值
     */
    private String value;
    /**
     * 过期时间(毫秒)
     */
    private Long expireTime;
}
