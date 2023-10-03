package org.itkk.udf.starter.file.db.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(callSuper = false)
@TableName("TF_DB_FILE")
public class DbFileInfoEntity {
    /**
     * 文件ID (非空)
     */
    @TableId("ID")
    private String id;

    /**
     * 应用名称 (非空)
     */
    @TableField("APPLICATION_NAME")
    private String applicationName;

    /**
     * 当前环境 (非空)
     */
    @TableField("PROFILES_ACTIVE")
    private String profilesActive;

    /**
     * 根路径代码 (非空)
     */
    @TableField("ROOT_PATH_CODE")
    private String rootPathCode;

    /**
     * 物理相对路径 (非空)
     */
    @TableField("PHYSICAL_RELATIVE_PATH")
    private String physicalRelativePath;

    /**
     * 物理文件名称 (非空)
     */
    @TableField("PHYSICAL_FILE_NAME")
    private String physicalFileName;

    /**
     * 文件名称 (非空)
     */
    @TableField("FILE_NAME")
    String fileName;

    /**
     * 文件上下文类型 (非空)
     */
    @TableField("CONTENT_TYPE")
    private String contentType;

    /**
     * 文件长度 (非空)
     */
    @TableField("FILE_SIZE")
    private Long fileSize;

    /**
     * 文件后缀
     */
    @TableField("FILE_SUFFIX")
    private String fileSuffix;

    /**
     * 业务路径
     */
    @TableField("BUSINESS_PATH")
    String businessPath;

    /**
     * PDF预览文件ID
     */
    @TableField("PREVIEW_PDF_FILE_ID")
    private String previewPdfFileId;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建时间(这里单词写错,多多包涵)
     */
    @TableField("CRETE_DATE")
    private Date creteDate;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_DATE")
    private Date updateDate;
}
