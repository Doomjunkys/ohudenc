package org.itkk.udf.qrcode;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import org.itkk.udf.qrcode.domain.QrCodeRequest;
import org.itkk.udf.qrcode.service.QrcodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * QrCodeTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QrcodeService.class)
public class QrCodeTest {

    /**
     * qrcodeService
     */
    @Autowired
    private QrcodeService qrcodeService;

    @Test
    public void testDepaseQrCode() throws FormatException, ChecksumException, NotFoundException, IOException {
        String img = "https://static.oschina.net/uploads/img/201707/09205944_0PzS.jpg";
        String ans = qrcodeService.decode(img);
        System.out.println(ans);
    }

    @Test
    public void testGenQrCode1() {
        String msg = "http://itkk.org:81";

        try {
            QrCodeRequest request = new QrCodeRequest();
            request.setContent(msg);
            String out = qrcodeService.parse(request);
            System.out.println("<img src=\"data:image/png;base64," + out + "\" />");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
