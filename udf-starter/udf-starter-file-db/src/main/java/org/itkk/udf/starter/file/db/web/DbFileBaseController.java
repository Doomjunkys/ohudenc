package org.itkk.udf.starter.file.db.web;

import org.itkk.udf.starter.core.RestResponse;
import org.itkk.udf.starter.core.exception.ParameterValidException;
import org.itkk.udf.starter.file.db.dto.DbFileInfoDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class DbFileBaseController extends DbFileBase {

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
     * @param id     id
     * @param width  width
     * @param height height
     * @throws IOException IOException
     */
    @GetMapping("preview/{id}")
    public void preview(@PathVariable String id, @RequestParam(name = "width", required = false) Integer width, @RequestParam(name = "height", required = false) Integer height) throws IOException {
        //设置头信息
        this.setResponseHeader(id, true);
        //下载
        dbFileService.download(id, width, height, response.getOutputStream());
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
        dbFileService.download(id, null, null, response.getOutputStream());
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
}
