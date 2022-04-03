package org.itkk.udf.qrcode;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import org.itkk.udf.qrcode.domain.QrCodeRequest;
import org.itkk.udf.qrcode.service.QrcodeService;
import org.junit.Test;

import java.io.IOException;

/**
 * QrCodeTest
 */
public class QrCodeTest {

    @Test
    public void testDepaseQrCode() throws FormatException, ChecksumException, NotFoundException, IOException {
        String img = "https://static.oschina.net/uploads/img/201707/09205944_0PzS.jpg";
        String ans = new QrcodeService().decode(img);
        System.out.println(ans);
    }

    @Test
    public void testGenQrCode1() {
        String msg = "http://itkk.org:81";

        try {
            QrCodeRequest request = new QrCodeRequest();
            request.setContent(msg);
            String out = new QrcodeService().parse(request);
            System.out.println("<img src=\"data:image/png;base64," + out + "\" />");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
