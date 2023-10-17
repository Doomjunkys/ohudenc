package org.itkk.udf.api.common.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.itkk.udf.api.common.entity.ParameterEntity;
import org.itkk.udf.api.common.repository.IParameterRepository;
import org.itkk.udf.starter.core.exception.ParameterValidException;
import org.itkk.udf.starter.core.exception.SystemRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ParameterService {

    /**
     * iParameterRepository
     */
    @Autowired
    private IParameterRepository iParameterRepository;

    /**
     * internalObjectMapper
     */
    @Autowired
    @Qualifier("internalObjectMapper")
    private ObjectMapper internalObjectMapper;

    /**
     * 批量返回String类型的内容
     *
     * @param codes codes
     * @return
     */
    public Map<String, String> getStringBatch(List<String> codes) {
        //定义返回值
        final Map<String, String> rv = new HashMap<>();
        //判空
        if (CollectionUtils.isNotEmpty(codes)) {
            //去重
            codes = codes.stream().distinct().collect(Collectors.toList());
            //遍历
            codes.forEach(code -> {
                try {
                    rv.put(code, this.getString(code));
                } catch (Exception e) {
                    rv.put(code, e.getMessage());
                }
            });
        }
        //返回
        return rv;
    }

    /**
     * 返回String类型的内容
     *
     * @param code code
     * @return String
     */
    public String getString(String code) {
        return this.getContent(code);
    }

    /**
     * 返回json类型的内容
     *
     * @param code code
     * @param <T>  <T>
     * @return T
     * @throws IOException IOException
     */
    public <T> T getJson(String code, TypeReference<T> typeReference) {
        try {
            //获得内容
            String content = this.getContent(code);
            //判空
            if (StringUtils.isBlank(code)) {
                return null;
            }
            //转换 & 返回
            return internalObjectMapper.readValue(content, typeReference);
        } catch (Exception e) {
            throw new SystemRuntimeException(e);
        }
    }

    /**
     * 获得内容
     *
     * @param code code
     * @return String
     */
    private String getContent(String code) {
        //获得数据
        ParameterEntity parameterEntity = iParameterRepository.selectById(code);
        //判空
        if (parameterEntity == null) {
            throw new ParameterValidException("代码为" + code + "的变量不存在");
        }
        //返回
        return parameterEntity.getContent();
    }

}
