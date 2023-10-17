package org.itkk.udf.api.common.web;

import com.fasterxml.jackson.core.type.TypeReference;
import org.itkk.udf.api.common.service.ParameterService;
import org.itkk.udf.starter.core.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public abstract class ParameterBaseController {

    /**
     * parameterService
     */
    @Autowired
    private ParameterService parameterService;

    /**
     * 批量返回String类型的内容
     *
     * @param codes codes
     * @return RestResponse<Map < String, String>>
     */
    @PostMapping("string/batch")
    public RestResponse<Map<String, String>> getStringBatch(@RequestBody List<String> codes) {
        return new RestResponse<>(parameterService.getStringBatch(codes));
    }

    /**
     * 返回String类型的内容
     *
     * @param code code
     * @return RestResponse<String>
     */
    @GetMapping("string/{code}")
    public RestResponse<String> getString(@PathVariable String code) {
        return new RestResponse<>(parameterService.getString(code));
    }

    /**
     * 返回json类型的map内容
     *
     * @param code code
     * @return RestResponse<Map < String, Object>>
     */
    @GetMapping("json/map/{code}")
    public RestResponse<Map<String, Object>> getJsonMap(@PathVariable String code) {
        return new RestResponse<>(parameterService.getJson(code, new TypeReference<Map<String, Object>>() {
        }));
    }

    /**
     * 返回json类型的array内容
     *
     * @param code code
     * @return RestResponse<List < Map < String, Object>>>
     */
    @GetMapping("json/array/{code}")
    public RestResponse<List<Map<String, Object>>> getJsonArray(@PathVariable String code) {
        return new RestResponse<>(parameterService.getJson(code, new TypeReference<List<Map<String, Object>>>() {
        }));
    }

}
