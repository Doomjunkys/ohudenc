/**
 * FileController.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.file.aliyun.oss.web;

import org.itkk.udf.core.RestResponse;
import org.itkk.udf.file.aliyun.oss.PostObjectPolicy;
import org.itkk.udf.file.aliyun.oss.domain.PolicyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 描述 : AliyunOssController
 *
 * @author Administrator
 */
@RestController
public class AliyunOssController implements IAliuyinOssController {

    /**
     * postObjectPolicy
     */
    @Autowired
    private PostObjectPolicy postObjectPolicy;

    @Override
    public RestResponse<PolicyResult> policy(@PathVariable String code) throws IOException {
        return new RestResponse<>(postObjectPolicy.getPolicy(code));
    }
}
