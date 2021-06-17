/**
 * ParameterValidErrorMessage.java
 * Created at 2016-11-05
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.core.exception;

import java.io.Serializable;
import java.util.List;

import org.springframework.validation.ObjectError;

/**
 * 描述 : ParameterValidErrorMessage
 *
 * @author wangkang
 *
 */
public class ParameterValidErrorMessage implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 参数静态校验错误信息
   */
  private List<ObjectError> allErrors;

  /**
   * 描述 : 构造函数
   *
   * @param parameterValidException parameterValidException
   */
  public ParameterValidErrorMessage(ParameterValidException parameterValidException) {
    allErrors = parameterValidException.getAllErrors();
  }

  /**
   * 描述 : 获取allErrors
   *
   * @return the allErrors
   */
  public List<ObjectError> getAllErrors() {
    return allErrors;
  }

  /**
   * 描述 : 设置allErrors
   *
   * @param allErrors the allErrors to set
   */
  public void setAllErrors(List<ObjectError> allErrors) {
    this.allErrors = allErrors;
  }

}
