package org.itkk.udf.starter.file.db.web;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.itkk.udf.starter.core.CoreConstant;
import org.itkk.udf.starter.core.CoreProperties;
import org.itkk.udf.starter.core.exception.ParameterValidException;
import org.itkk.udf.starter.file.db.DbFileProperties;
import org.itkk.udf.starter.file.db.dto.DbFileInfoDto;
import org.itkk.udf.starter.file.db.service.DbFileService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public abstract class DbFileBase {

    /**
     * dbFileService
     */
    @Autowired
    protected DbFileService dbFileService;

    /**
     * dbFileProperties
     */
    @Autowired
    protected DbFileProperties dbFileProperties;

    /**
     * coreProperties
     */
    @Autowired
    protected CoreProperties coreProperties;

    /**
     * response
     */
    @Autowired
    protected HttpServletResponse response;

    /**
     * request
     */
    @Autowired
    protected HttpServletRequest request;

    /**
     * 设置头信息
     *
     * @param id      id
     * @param preview preview
     * @throws UnsupportedEncodingException
     */
    protected void setResponseHeader(String id, Boolean preview) throws UnsupportedEncodingException {
        //获得文件信息
        DbFileInfoDto fileInfo = dbFileService.get(id);
        //判空
        if (fileInfo == null) {
            throw new ParameterValidException("文件" + id + "不存在");
        }
        //rootPath判空
        if (MapUtils.isEmpty(dbFileProperties.getMapping()) || !dbFileProperties.getMapping().containsKey(fileInfo.getRootPathCode())) {
            throw new ParameterValidException("文件路径映射" + fileInfo.getRootPathCode() + "不存在");
        }
        //清空response
        response.reset();
        //设置response
        response.setContentLengthLong(fileInfo.getFileSize());
        response.setCharacterEncoding(CoreConstant.CHARACTER_SET);
        response.setContentType(fileInfo.getContentType());
        response.setDateHeader("Expires", 0L);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        //response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        //判断是否预览 (预览则要限定一下上下文类型){
        if (preview) {
            if (StringUtils.isNotBlank(dbFileProperties.getPreviewSupportContentType())) {
                if (!dbFileProperties.getPreviewSupportContentType().contains(fileInfo.getContentType())) {
                    throw new ParameterValidException("不支持[" + fileInfo.getContentType() + "]此上下文类型的预览");
                }
            }
        }
        //判断是否预览 (预览则不用设置文件名)
        if (!preview) {
            //设置文件名
            String fileName = URLEncoder.encode(fileInfo.getFileName(), CoreConstant.CHARACTER_SET);
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            } else {
                response.setHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'" + fileName);
            }
        }
    }
}
