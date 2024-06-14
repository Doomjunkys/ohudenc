package org.itkk.udf.api.general.web;


import org.itkk.udf.api.common.CommonConstant;
import org.itkk.udf.starter.core.exception.ParameterValidException;
import org.itkk.udf.starter.file.db.DbFileConstant;
import org.itkk.udf.starter.file.db.dto.DbFileInfoDto;
import org.itkk.udf.starter.file.db.web.DbFileBase;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = CommonConstant.URL_ROOT_WEB_PUBLIC + "file/")
public class FilePublicController extends DbFileBase {
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
        //获得文件信息
        DbFileInfoDto dbFileInfoDto = dbFileService.get(id);
        //判空
        if (dbFileInfoDto == null) {
            throw new ParameterValidException("文件ID:" + id + "对应的文件不存在");
        }
        //判断是否公共文件
        if (DbFileConstant.PUBLIC_ROOT_PATH_CODE.equals(dbFileInfoDto.getRootPathCode())) {
            throw new ParameterValidException("文件ID:" + id + "不是公共文件");
        }
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
        //获得文件信息
        DbFileInfoDto dbFileInfoDto = dbFileService.get(id);
        //判空
        if (dbFileInfoDto == null) {
            throw new ParameterValidException("文件ID:" + id + "对应的文件不存在");
        }
        //判断是否公共文件
        if (DbFileConstant.PUBLIC_ROOT_PATH_CODE.equals(dbFileInfoDto.getRootPathCode())) {
            throw new ParameterValidException("文件ID:" + id + "不是公共文件");
        }
        //设置头信息
        this.setResponseHeader(id, false);
        //下载
        dbFileService.download(id, null, null, response.getOutputStream());
    }
}
