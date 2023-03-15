package org.itkk.udf.weixin.mp.api.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Menu
 */
@Data
public class Menu implements Serializable {

    /**
     * ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 二级菜单数组，个数应为1~5个
     */
    private List<Menu> sub_button; //NOSONAR

    /**
     * 菜单的响应动作类型，view表示网页类型，click表示点击类型，miniprogram表示小程序类型
     */
    private String type;

    /**
     * 菜单标题，不超过16个字节，子菜单不超过60个字节
     */
    private String name;

    /**
     * 菜单KEY值，用于消息接口推送，不超过128字节(click等点击类型必须)
     */
    private String key;

    /**
     * 网页链接，用户点击菜单可打开链接，不超过1024字节(view、miniprogram类型必须)
     */
    private String url;

    /**
     * 调用新增永久素材接口返回的合法media_id(media_id类型和view_limited类型必须)
     */
    private String media_id; //NOSONAR

    /**
     * 小程序的appid（仅认证公众号可配置） (miniprogram类型必须)
     */
    private String appid;

    /**
     * 小程序的页面路径 (miniprogram类型必须)
     */
    private String pagepath;

}
