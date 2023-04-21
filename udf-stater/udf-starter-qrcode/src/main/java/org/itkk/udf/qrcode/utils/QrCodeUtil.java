package org.itkk.udf.qrcode.utils;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.qrcode.option.BgImgStyle;
import org.itkk.udf.qrcode.option.BitMatrixEx;
import org.itkk.udf.qrcode.option.QrCodeOptions;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * QrCodeUtil
 */
@Slf4j
public class QrCodeUtil {

    /**
     * QUIET_ZONE_SIZE
     */
    private static final int QUIET_ZONE_SIZE = 4;

    /**
     * 私有化构造函数
     */
    private QrCodeUtil() {

    }

    /**
     * 对 zxing 的 QRCodeWriter 进行扩展, 解决白边过多的问题
     *
     * @param qrCodeConfig qrCodeConfig
     * @return BitMatrixEx
     * @throws WriterException WriterException
     */
    public static BitMatrixEx encode(QrCodeOptions qrCodeConfig) throws WriterException {
        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
        int quietZone = 1;
        if (qrCodeConfig.getHints() != null) {
            if (qrCodeConfig.getHints().containsKey(EncodeHintType.ERROR_CORRECTION)) {
                errorCorrectionLevel = ErrorCorrectionLevel.valueOf(qrCodeConfig.getHints().get(EncodeHintType.ERROR_CORRECTION).toString());
            }
            if (qrCodeConfig.getHints().containsKey(EncodeHintType.MARGIN)) {
                quietZone = Integer.parseInt(qrCodeConfig.getHints().get(EncodeHintType.MARGIN).toString());
            }
            if (quietZone > QUIET_ZONE_SIZE) {
                quietZone = QUIET_ZONE_SIZE;
            } else if (quietZone < 0) {
                quietZone = 0;
            }
        }
        QRCode code = Encoder.encode(qrCodeConfig.getMsg(), errorCorrectionLevel, qrCodeConfig.getHints());
        return renderResult(code, qrCodeConfig.getW(), qrCodeConfig.getH(), quietZone);
    }

    /**
     * 对 zxing 的 QRCodeWriter 进行扩展, 解决白边过多的问题
     *
     * @param code      code
     * @param width     width
     * @param height    height
     * @param quietZone quietZone 取值 [0, 4]
     * @return BitMatrixEx
     */
    private static BitMatrixEx renderResult(QRCode code, int width, int height, int quietZone) {
        final int num2 = 2;
        ByteMatrix input = code.getMatrix();
        if (input == null) {
            throw new IllegalStateException();
        }
        // xxx 二维码宽高相等, 即 qrWidth == qrHeight
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        int qrWidth = inputWidth + (quietZone * num2);
        int qrHeight = inputHeight + (quietZone * num2);
        // 白边过多时, 缩放
        int minSize = Math.min(width, height);
        int scale = calculateScale(qrWidth, minSize);
        if (scale > 0) {
            if (log.isDebugEnabled()) {
                log.debug("qrCode scale enable! scale: {}, qrSize:{}, expectSize:{}x{}", scale, qrWidth, width, height);
            }
            int padding;
            int tmpValue;
            // 计算边框留白
            padding = (minSize - qrWidth * scale) / QUIET_ZONE_SIZE * quietZone;
            tmpValue = qrWidth * scale + padding;
            if (width == height) {
                width = tmpValue; //NOSONAR
                height = tmpValue;  //NOSONAR
            } else if (width > height) {
                width = width * tmpValue / height;  //NOSONAR
                height = tmpValue;  //NOSONAR
            } else {
                height = height * tmpValue / width;  //NOSONAR
                width = tmpValue;  //NOSONAR
            }
        }
        int outputWidth = Math.max(width, qrWidth);
        int outputHeight = Math.max(height, qrHeight);
        int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
        int leftPadding = (outputWidth - (inputWidth * multiple)) / num2;
        int topPadding = (outputHeight - (inputHeight * multiple)) / num2;
        BitMatrixEx res = new BitMatrixEx();
        res.setByteMatrix(input);
        res.setLeftPadding(leftPadding);
        res.setTopPadding(topPadding);
        res.setMultiple(multiple);
        res.setWidth(outputWidth);
        res.setHeight(outputHeight);
        return res;
    }

    /**
     * 根据二维码配置 & 二维码矩阵生成二维码图片
     *
     * @param qrCodeConfig qrCodeConfig
     * @param bitMatrix    bitMatrix
     * @return BufferedImage
     */
    public static BufferedImage toBufferedImage(QrCodeOptions qrCodeConfig, BitMatrixEx bitMatrix) {
        int qrCodeWidth = bitMatrix.getWidth();
        int qrCodeHeight = bitMatrix.getHeight();
        BufferedImage qrCode = ImageUtil.drawQrInfo(qrCodeConfig, bitMatrix);
        // 若二维码的实际宽高和预期的宽高不一致, 则缩放
        int realQrCodeWidth = qrCodeConfig.getW();
        int realQrCodeHeight = qrCodeConfig.getH();
        if (qrCodeWidth != realQrCodeWidth || qrCodeHeight != realQrCodeHeight) {
            BufferedImage tmp = new BufferedImage(realQrCodeWidth, realQrCodeHeight, BufferedImage.TYPE_INT_RGB);
            tmp.getGraphics().drawImage(qrCode.getScaledInstance(realQrCodeWidth, realQrCodeHeight, Image.SCALE_SMOOTH), 0, 0, null);
            qrCode = tmp;
        }
        /**
         * 说明
         *  在覆盖模式下，先设置二维码的透明度，然后绘制在背景图的正中央，最后绘制logo，这样保证logo不会透明，显示清晰
         *  在填充模式下，先绘制logo，然后绘制在背景的指定位置上；若先绘制背景，再绘制logo，则logo大小偏移量的计算会有问题
         */
        boolean logoDrawed = false;
        // 绘制背景图
        if (qrCodeConfig.getBgImgOptions() != null) {
            if (qrCodeConfig.getBgImgOptions().getBgImgStyle() == BgImgStyle.FILL
                    && qrCodeConfig.getLogoOptions() != null) {
                // 此种模式，先绘制logo
                qrCode = ImageUtil.drawLogo(qrCode, qrCodeConfig.getLogoOptions());
                logoDrawed = true;
            }

            qrCode = ImageUtil.drawBackground(qrCode, qrCodeConfig.getBgImgOptions());
        }
        // 插入logo
        if (qrCodeConfig.getLogoOptions() != null && !logoDrawed) {
            qrCode = ImageUtil.drawLogo(qrCode, qrCodeConfig.getLogoOptions());
        }
        return qrCode;
    }

    /**
     * 如果留白超过15% , 则需要缩放
     * (15% 可以根据实际需要进行修改)
     *
     * @param qrCodeSize 二维码大小
     * @param expectSize 期望输出大小
     * @return 返回缩放比例, <= 0 则表示不缩放, 否则指定缩放参数
     */
    private static int calculateScale(int qrCodeSize, int expectSize) {
        if (qrCodeSize >= expectSize) {
            return 0;
        }
        int scale = expectSize / qrCodeSize;
        int abs = expectSize - scale * qrCodeSize;
        if (abs < expectSize * 0.15) {
            return 0;
        }
        return scale;
    }

}
