/**
 * FileController.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.file.aliyun.oss.web;

import org.itkk.udf.core.RestResponse;
import org.itkk.udf.file.aliyun.oss.OssWarpper;
import org.itkk.udf.file.aliyun.oss.domain.OssParam;
import org.itkk.udf.file.aliyun.oss.domain.PolicyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

/**
 * 描述 : AliyunOssController
 *
 * @author Administrator
 */
@RestController
public class AliyunOssController implements IAliuyinOssController {

    /**
     * ossWarpper
     */
    @Autowired
    private OssWarpper ossWarpper;

    @Override
    public RestResponse<PolicyResult> policy(@PathVariable String code) throws IOException {
        return new RestResponse<>(ossWarpper.getPolicy(code));
    }

    @Override
    public RestResponse<String> presignedUrl(@RequestBody @Valid OssParam ossParam) {
        return new RestResponse<>(ossWarpper.getPresignedUrl(ossParam.getCode(), ossParam.getObjectKey()));
    }

    @Override
    public RestResponse<String> delete(@RequestBody @Valid OssParam ossParam) {
        ossWarpper.delete(ossParam.getCode(), ossParam.getObjectKey());
        return new RestResponse<>();
    }

    @Override
    public RestResponse<Boolean> checkExist(@RequestBody @Valid OssParam ossParam) {
        return new RestResponse<>(ossWarpper.checkExist(ossParam.getCode(), ossParam.getObjectKey()));
    }
}
