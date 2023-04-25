package org.itkk.udf.weixin.mp.api.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * MenuGroup
 */
@Data
public class MenuGroup implements Serializable {

    /**
     * ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 一级菜单数组，个数应为1~3个
     */
    private List<Menu> button;

}
