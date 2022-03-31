package org.itkk.udf.qrcode.web;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.itkk.udf.core.RestResponse;
import org.itkk.udf.qrcode.domain.QrCodeRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 描述 : IQrcodeController
 *
 * @author Administrator
 */
@Api(value = "二维码服务", consumes = "application/json", produces = "application/json", protocols = "http")
@RequestMapping(value = "/service/qrcode")
public interface IQrcodeController {

    /**
     * 二维码内容识别
     *
     * @param file 文件
     * @return 结果
     * @throws IOException       IOException
     * @throws FormatException   FormatException
     * @throws ChecksumException ChecksumException
     * @throws NotFoundException NotFoundException
     */
    @ApiOperation(value = "QRCODE_1", notes = "二维码内容识别")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "form", name = "file", value = "文件", required = true, dataType = "__file")
    })
    @RequestMapping(value = "decode", method = RequestMethod.POST)
    RestResponse<String> decode(MultipartFile file) throws IOException, FormatException, ChecksumException, NotFoundException;

    /**
     * 二维码内容识别
     *
     * @param path 二维码地址
     * @return 结果
     * @throws IOException       IOException
     * @throws FormatException   FormatException
     * @throws ChecksumException ChecksumException
     * @throws NotFoundException NotFoundException
     */
    @ApiOperation(value = "QRCODE_2", notes = "二维码内容识别")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "path", value = "二维码地址", required = true, dataType = "string")
    })
    @RequestMapping(value = "decode", method = RequestMethod.GET)
    RestResponse<String> decode(String path) throws FormatException, ChecksumException, NotFoundException, IOException;

    /**
     * 生成二维码
     *
     * @param qrCodeRequest 参数
     * @return 结果
     * @throws IOException IOException
     * @throws WriterException WriterException
     */
    @ApiOperation(value = "QRCODE_3", notes = "生成二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号", required = true, dataType = "string")
    })
    @RequestMapping(value = "gen", method = RequestMethod.POST)
    RestResponse<String> parse(@RequestBody QrCodeRequest qrCodeRequest) throws IOException, WriterException;

}
