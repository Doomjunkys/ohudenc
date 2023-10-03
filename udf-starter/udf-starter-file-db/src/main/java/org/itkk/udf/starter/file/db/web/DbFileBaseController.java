package org.itkk.udf.starter.file.db.web;

import org.itkk.udf.starter.core.CoreConstant;
import org.itkk.udf.starter.core.CoreProperties;
import org.itkk.udf.starter.core.RestResponse;
import org.itkk.udf.starter.core.exception.ParameterValidException;
import org.itkk.udf.starter.file.db.DbFileProperties;
import org.itkk.udf.starter.file.db.dto.DbFileInfoDto;
import org.itkk.udf.starter.file.db.service.DbFileService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public abstract class DbFileBaseController {

    /**
     * dbFileService
     */
    @Autowired
    private DbFileService dbFileService;

    /**
     * dbFileProperties
     */
    @Autowired
    private DbFileProperties dbFileProperties;

    /**
     * coreProperties
     */
    @Autowired
    private CoreProperties coreProperties;

    /**
     * response
     */
    @Autowired
    private HttpServletResponse response;

    /**
     * request
     */
    @Autowired
    private HttpServletRequest request;

    /**
     * 获得当前用户ID
     *
     * @return String
     */
    public abstract String getCurrentUserId();

    /**
     * 单个文件上传(表单方式)
     *
     * @param rootPathCode      rootPathCode
     * @param businessPath      businessPath
     * @param genPreviewPdfFile genPreviewPdfFile
     * @param file              file
     * @return RestResponse<String>
     * @throws IOException IOException
     */
    @PostMapping("upload")
    public RestResponse<DbFileInfoDto> upload(String rootPathCode, String businessPath, Boolean genPreviewPdfFile, MultipartFile file) throws IOException {
        //判空
        if (file == null) {
            throw new ParameterValidException("文件不能为空");
        }
        //上传
        return new RestResponse<>(dbFileService.upload(rootPathCode, genPreviewPdfFile, businessPath, file.getOriginalFilename(), file.getContentType(), file.getInputStream(), this.getCurrentUserId()));
    }

    /**
     * 批量获得文件信息
     *
     * @param ids ids
     * @return RestResponse<Map < String, DbFileInfoDto>>
     */
    @PostMapping("get/batch")
    public RestResponse<Map<String, DbFileInfoDto>> getBatch(@RequestBody List<String> ids) {
        return new RestResponse<>(dbFileService.getBatch(ids));
    }

    /**
     * 获得文件信息
     *
     * @param id id
     * @return RestResponse<DbFileInfoDto>
     */
    @GetMapping("get/{id}")
    public RestResponse<DbFileInfoDto> get(@PathVariable String id) {
        return new RestResponse<>(dbFileService.get(id));
    }

    /**
     * 文件预览
     *
     * @param id id
     * @throws IOException IOException
     */
    @GetMapping("preview/{id}")
    public void preview(@PathVariable String id) throws IOException {
        //设置头信息
        this.setResponseHeader(id, true);
        //下载
        dbFileService.download(id, response.getOutputStream());
    }

    /**
     * 文件下载
     *
     * @param id id
     * @throws IOException IOException
     */
    @GetMapping("download/{id}")
    public void download(@PathVariable String id) throws IOException {
        //设置头信息
        this.setResponseHeader(id, false);
        //下载
        dbFileService.download(id, response.getOutputStream());
    }

    /**
     * 删除文件
     *
     * @param id id
     * @return RestResponse<String>
     * @throws IOException IOException
     */
    @DeleteMapping("delete/{id}")
    public RestResponse<String> delete(@PathVariable String id) throws IOException {
        dbFileService.delete(id);
        return RestResponse.success();
    }

    /**
     * 设置头信息
     *
     * @param id      id
     * @param preview preview
     * @throws UnsupportedEncodingException
     */
    private void setResponseHeader(String id, Boolean preview) throws UnsupportedEncodingException {
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
