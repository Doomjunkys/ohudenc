/**
 * MailInfo.java
 * Created at 2017-05-28
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.core.domain.mail;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述 : MailInfo
 *
 * @author Administrator
 *
 */
@ApiModel(description = "邮件信息")
public class MailInfo implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 接收地址
   */
  @ApiModelProperty(value = "接收地址", required = true, dataType = "string")
  @NotEmpty(message = "接收地址不能为空")
  private String[] to;

  /**
   * 描述 : 主题
   */
  @ApiModelProperty(value = "主题", required = true, dataType = "string")
  @NotEmpty(message = "主题不能为空")
  private String subject;

  /**
   * 描述 : 正文
   */
  @ApiModelProperty(value = "正文", required = true, dataType = "string")
  @NotEmpty(message = "正文不能为空")
  private String text;

  /**
   * 描述 : 是否html正文
   */
  @ApiModelProperty(value = "是否html正文(默认false)", required = true, dataType = "boolean")
  private Boolean isHtmlText = false;

  /**
   * 描述 : 保密抄送地址
   */
  @ApiModelProperty(value = "保密抄送地址", required = false, dataType = "string")
  private String[] bcc;

  /**
   * 描述 : 抄送地址
   */
  @ApiModelProperty(value = "抄送地址", required = false, dataType = "string")
  private String[] cc;

  /**
   * 描述 : 回复地址
   */
  @ApiModelProperty(value = "回复地址", required = false, dataType = "string")
  private String replyTo;

  /**
   * 描述 : 优先级
   */
  @ApiModelProperty(value = "优先级", required = false, dataType = "int")
  private Integer priority;

  /**
   * 描述 : 附件列表
   */
  @ApiModelProperty(value = "附件列表", required = false, dataType = "object")
  private List<AttachmentInfo> attachments;

  /**
   * 描述 : 静态资源列表
   */
  @ApiModelProperty(value = "静态资源列表", required = false, dataType = "object")
  private List<InlineInfo> inlines;

  /**
   * 描述 : 获取inlines
   *
   * @return the inlines
   */
  public List<InlineInfo> getInlines() {
    return inlines;
  }

  /**
   * 描述 : 设置inlines
   *
   * @param inlines the inlines to set
   */
  public void setInlines(List<InlineInfo> inlines) {
    this.inlines = inlines;
  }

  /**
   * 描述 : 获取isHtmlText
   *
   * @return the isHtmlText
   */
  public Boolean getIsHtmlText() {
    return isHtmlText;
  }

  /**
   * 描述 : 设置isHtmlText
   *
   * @param isHtmlText the isHtmlText to set
   */
  public void setIsHtmlText(Boolean isHtmlText) {
    this.isHtmlText = isHtmlText;
  }

  /**
   * 描述 : 获取to
   *
   * @return the to
   */
  public String[] getTo() {
    return to;
  }

  /**
   * 描述 : 设置to
   *
   * @param to the to to set
   */
  public void setTo(String[] to) {
    this.to = to;
  }

  /**
   * 描述 : 获取subject
   *
   * @return the subject
   */
  public String getSubject() {
    return subject;
  }

  /**
   * 描述 : 设置subject
   *
   * @param subject the subject to set
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * 描述 : 获取text
   *
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * 描述 : 设置text
   *
   * @param text the text to set
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * 描述 : 获取bcc
   *
   * @return the bcc
   */
  public String[] getBcc() {
    return bcc;
  }

  /**
   * 描述 : 设置bcc
   *
   * @param bcc the bcc to set
   */
  public void setBcc(String[] bcc) {
    this.bcc = bcc;
  }

  /**
   * 描述 : 获取cc
   *
   * @return the cc
   */
  public String[] getCc() {
    return cc;
  }

  /**
   * 描述 : 设置cc
   *
   * @param cc the cc to set
   */
  public void setCc(String[] cc) {
    this.cc = cc;
  }

  /**
   * 描述 : 获取replyTo
   *
   * @return the replyTo
   */
  public String getReplyTo() {
    return replyTo;
  }

  /**
   * 描述 : 设置replyTo
   *
   * @param replyTo the replyTo to set
   */
  public void setReplyTo(String replyTo) {
    this.replyTo = replyTo;
  }

  /**
   * 描述 : 获取priority
   *
   * @return the priority
   */
  public Integer getPriority() {
    return priority;
  }

  /**
   * 描述 : 设置priority
   *
   * @param priority the priority to set
   */
  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  /**
   * 描述 : 获取attachments
   *
   * @return the attachments
   */
  public List<AttachmentInfo> getAttachments() {
    return attachments;
  }

  /**
   * 描述 : 设置attachments
   *
   * @param attachments the attachments to set
   */
  public void setAttachments(List<AttachmentInfo> attachments) {
    this.attachments = attachments;
  }

}
