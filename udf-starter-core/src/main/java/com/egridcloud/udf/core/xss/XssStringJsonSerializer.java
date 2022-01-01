/**
 * XssStringJsonSerializer.java
 * Created at 2016-09-19
 * Created by wangkang
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package com.egridcloud.udf.core.xss;

import java.io.IOException;

import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 描述 : 基于xss的JsonSerializer
 *
 * @author wangkang
 *
 */
public class XssStringJsonSerializer extends JsonSerializer<String> {

  @Override
  public Class<String> handledType() {
    return String.class;
  }

  @Override
  public void serialize(String value, JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider) throws IOException {
    if (value != null) {
      String encodedValue = HtmlUtils.htmlEscape(value);
      jsonGenerator.writeString(encodedValue);
    }
  }

}
