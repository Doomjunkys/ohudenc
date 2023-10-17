package org.itkk.udf.api.general.web;

import org.itkk.udf.api.common.CommonConstant;
import org.itkk.udf.api.common.web.ParameterBaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = CommonConstant.URL_ROOT_WEB_PRIVATE + "parameter/")
public class ParameterController extends ParameterBaseController {

}
