package org.itkk.udf.starter.file.db.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(callSuper = false)
public class DbFileInfoDto {
    /**
     * 文件ID (非空)
     */
    private String id;

    /**
     * 应用名称 (非空)
     */
    private String applicationName;

    /**
     * 当前环境 (非空)
     */
    private String profilesActive;

    /**
     * 根路径代码 (非空)
     */
    private String rootPathCode;

    /**
     * 物理相对路径 (非空)
     */
    private String physicalRelativePath;

    /**
     * 物理文件名称 (非空)
     */
    private String physicalFileName;

    /**
     * 文件名称 (非空)
     */
    String fileName;

    /**
     * 文件上下文类型 (非空)
     */
    private String contentType;

    /**
     * 文件长度 (非空)
     */
    private Long fileSize;

    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 业务路径
     */
    String businessPath;

    /**
     * PDF预览文件ID
     */
    private String previewPdfFileId;

    /**
     * 查看链接
     */
    private String viewLink;

    /**
     * 内嵌查看链接
     */
    private String embedViewLink;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间(这里单词写错,多多包涵)
     */
    private Date creteDate;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateDate;
}
