/**
 * MailService.java
 * Created at 2017-05-28
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.mail.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.egridcloud.udf.core.RestResponse;
import com.egridcloud.udf.mail.MailProperties;
import com.egridcloud.udf.mail.domain.AttachmentInfo;
import com.egridcloud.udf.mail.domain.MailInfo;
import com.egridcloud.udf.mail.vo.file.FileInfo;
import com.egridcloud.udf.rms.Rms;

/**
 * 描述 : MailService
 *
 * @author Administrator
 *
 */
@Service
public class MailService {

  /**
   * 描述 : mailSender
   */
  @Autowired
  private JavaMailSender mailSender;

  /**
   * 描述 : mailProperties
   */
  @Autowired
  private MailProperties mailProperties;

  /**
   * 描述 : rms
   */
  @Autowired
  private Rms rms;

  /**
   * 描述 : 发送邮件
   *
   * @param mailInfo 邮件信息
   * @throws MessagingException 异常
   */
  @Async
  public void send(MailInfo mailInfo) throws MessagingException {
    //创建消息
    MimeMessage message = mailSender.createMimeMessage();
    //判断是否有附件
    boolean multipart = CollectionUtils.isEmpty(mailInfo.getAttachments()) ? false : true;
    //构造消息mailInfo
    MimeMessageHelper helper = new MimeMessageHelper(message, multipart);
    //必要字段
    helper.setFrom(mailProperties.getFrom());
    helper.setSentDate(new Date());
    helper.setTo(mailInfo.getTo());
    helper.setSubject(mailInfo.getSubject());
    helper.setText(mailInfo.getText(), mailInfo.getIsHtmlText());
    //秘密抄送
    if (ArrayUtils.isNotEmpty(mailInfo.getBcc())) {
      helper.setBcc(mailInfo.getBcc());
    }
    //抄送
    if (ArrayUtils.isNotEmpty(mailInfo.getCc())) {
      helper.setCc(mailInfo.getCc());
    }
    //优先级
    if (mailInfo.getPriority() != null) {
      helper.setPriority(mailInfo.getPriority());
    }
    //邮件回执
    if (StringUtils.isNotBlank(mailInfo.getReplyTo())) {
      helper.setReplyTo(mailInfo.getReplyTo());
    }
    //处理附件
    if (CollectionUtils.isNotEmpty(mailInfo.getAttachments())) {
      for (AttachmentInfo attachment : mailInfo.getAttachments()) {
        if (StringUtils.isNotBlank(attachment.getFileId())) {
          //获得必要参数
          String fileId = attachment.getFileId();
          String name = attachment.getName();
          //构造参数
          Map<String, String> uriVariables = new HashMap<>();
          uriVariables.put("fileId", fileId);
          //获得文件信息
          ResponseEntity<RestResponse<FileInfo>> fileInfo = rms.call("FILE_4", null, null,
              new ParameterizedTypeReference<RestResponse<FileInfo>>() {
              }, uriVariables);
          //获得文件
          ResponseEntity<byte[]> file =
              rms.call("FILE_3", null, null, new ParameterizedTypeReference<byte[]>() {
              }, uriVariables);
          //构造resource
          ByteArrayResource bar = new ByteArrayResource(file.getBody());
          //判断附件名称
          name = StringUtils.isNoneBlank(name) ? name : fileInfo.getBody().getResult().getName();
          //添加附件
          helper.addAttachment(name, bar);
        }
      }
    }
    //发送消息
    mailSender.send(message);
  }

}
