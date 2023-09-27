package org.itkk.udf.starter.core.xss;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.itkk.udf.starter.core.CoreConstant;
import org.itkk.udf.starter.core.CoreUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class XssStringJsonSerializer extends JsonSerializer<String> {
    @Override
    public Class<String> handledType() {
        return String.class;
    }

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (value != null) {
            HttpServletRequest httpServletRequest = CoreUtil.getHttpServletRequest();
            if (httpServletRequest != null && httpServletRequest.getRequestURI().contains(CoreConstant.PARAMETER_STRING_BATCH_PATH_ROOT)) {
                jsonGenerator.writeString(value);
            } else {
                jsonGenerator.writeString(XssUtil.xssEncode(value));
            }
        }
    }
}
