package org.itkk.udf.starter.core.trace;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(callSuper = false)
public class TraceDto implements Serializable {
    private static final long serialVersionUID = 6660352674554200657L; //ID
    private String profilesActive; //环境
    private String traceId; //追踪ID
    private String url; //请求地址
    private String method; //请求方法
    private String queryString; //请求参数(GET)
    private Map<String, String[]> parameterMap; //请求参数(FORM)
    private String requestBody; //请求体
    private String responseBody; //响应体
    private String requestContentType; //请求上下文类型
    private String responseContentType; //响应上下文类型
    private Integer httpStatus; //http状态
    private Long startTime; //请求开始时间
    private Long endTime; //请求结束时间
    private Long timeCost; //请求耗时
}
