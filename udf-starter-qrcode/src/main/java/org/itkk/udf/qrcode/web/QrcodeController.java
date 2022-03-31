package org.itkk.udf.qrcode.web;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.itkk.udf.core.RestResponse;
import org.itkk.udf.qrcode.domain.QrCodeRequest;
import org.itkk.udf.qrcode.service.QrcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * QrcodeController
 */
@RestController
public class QrcodeController implements IQrcodeController {

    /**
     * qrcodeService
     */
    @Autowired
    private QrcodeService qrcodeService;

    @Override
    public RestResponse<String> decode(MultipartFile file) throws IOException, FormatException, ChecksumException, NotFoundException {
        return new RestResponse<>(this.qrcodeService.decode(ImageIO.read(file.getInputStream())));
    }

    @Override
    public RestResponse<String> decode(String path) throws FormatException, ChecksumException, NotFoundException, IOException {
        return new RestResponse<>(this.qrcodeService.decode(path));
    }

    @Override
    public RestResponse<String> parse(@RequestBody QrCodeRequest qrCodeRequest) throws IOException, WriterException {
        return new RestResponse<>(this.qrcodeService.parse(qrCodeRequest));
    }
}
