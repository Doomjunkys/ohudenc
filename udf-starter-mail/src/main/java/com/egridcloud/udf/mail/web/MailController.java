/**
 * MailController.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.mail.web;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.egridcloud.udf.core.RestResponse;
import com.egridcloud.udf.core.exception.ErrorCode;
import com.egridcloud.udf.core.exception.ParameterValidException;
import com.egridcloud.udf.mail.domain.MailInfo;
import com.egridcloud.udf.mail.service.MailService;

/**
 * 描述 : MailController
 *
 * @author Administrator
 *
 */
@RestController
public class MailController implements IMailController {

  /**
   * 描述 : mailService
   */
  @Autowired
  private MailService mailService;

  @Override
  public RestResponse<String> send(@Valid @RequestBody MailInfo mailInfo, BindingResult result)
      throws MessagingException {
    //校验
    if (result.hasErrors()) {
      throw new ParameterValidException(ErrorCode.PARAMETER_VALID_ERROR.value(),
          result.getAllErrors());
    }
    //发送
    mailService.send(mailInfo);
    //返回
    return new RestResponse<>();
  }

}
