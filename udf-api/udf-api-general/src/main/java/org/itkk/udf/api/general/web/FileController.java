package org.itkk.udf.api.general.web;


import org.itkk.udf.api.common.CommonConstant;
import org.itkk.udf.api.common.CommonUtil;
import org.itkk.udf.starter.file.db.web.DbFileBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = CommonConstant.URL_ROOT_WEB_PRIVATE + "file/")
public class FileController extends DbFileBaseController {

    /**
     * httpServletRequest
     */
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public String getCurrentUserId() {
        return CommonUtil.getUser(httpServletRequest).getUserId();
    }
}
