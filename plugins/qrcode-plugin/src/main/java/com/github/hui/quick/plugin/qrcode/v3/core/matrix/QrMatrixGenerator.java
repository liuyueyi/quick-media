package com.github.hui.quick.plugin.qrcode.v3.core.matrix;

import com.github.hui.quick.plugin.qrcode.v3.req.LogoOptions;
import com.github.hui.quick.plugin.qrcode.v3.req.QrCodeV3Options;
import com.github.hui.quick.plugin.qrcode.wrapper.BitMatrixEx;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import org.apache.commons.lang3.BooleanUtils;

import java.util.Map;

/**
 * @author YiHui
 * @date 2022/8/2
 */
public class QrMatrixGenerator {
    /**
     * 最大的留白
     */
    private static final int MAX_QUIET_ZONE_SIZE = 4;

    /**
     * 对 zxing 的 QRCodeWriter 进行扩展, 解决白边过多的问题
     * <p/>
     * 源码参考 {@link com.google.zxing.qrcode.QRCodeWriter#encode(String, BarcodeFormat, int, int, Map)}
     */
    public static BitMatrixEx calculateMatrix(QrCodeV3Options qrCodeConfig) throws WriterException {
        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
        int quietZone = 1;
        if (qrCodeConfig.getHints() != null) {
            if (qrCodeConfig.getHints().containsKey(EncodeHintType.ERROR_CORRECTION)) {
                errorCorrectionLevel = ErrorCorrectionLevel.valueOf(qrCodeConfig.getHints().get(EncodeHintType.ERROR_CORRECTION).toString());
            }
            if (qrCodeConfig.getHints().containsKey(EncodeHintType.MARGIN)) {
                quietZone = Integer.parseInt(qrCodeConfig.getHints().get(EncodeHintType.MARGIN).toString());
            }
            if (quietZone > MAX_QUIET_ZONE_SIZE) {
                quietZone = MAX_QUIET_ZONE_SIZE;
            } else if (quietZone < 0) {
                quietZone = 0;
            }
        }

        QRCode code = Encoder.encode(qrCodeConfig.getMsg(), errorCorrectionLevel, qrCodeConfig.getHints());
        BitMatrixEx bitMatrixEx = renderResult(code, qrCodeConfig.getW(), qrCodeConfig.getH(), quietZone);
        clearLogo(bitMatrixEx, qrCodeConfig.getLogoOptions());
        return bitMatrixEx;
    }

    private static void clearLogo(BitMatrixEx bitMatrixEx, LogoOptions logoOptions) {
        if (logoOptions == null || BooleanUtils.isNotTrue(logoOptions.getClearLogoArea())) {
            return;
        }

        // 将logo所占的点阵区间，全部设置为0，避免出现logo覆盖时，渲染处被部分覆盖的问题
        int rate = logoOptions.getRate() / 2;
        int width = bitMatrixEx.getByteMatrix().getWidth();
        int height = bitMatrixEx.getByteMatrix().getHeight();
        int logoWidth = (int) Math.ceil(width / (float) rate);
        int logoHeight = (int) Math.ceil(height / (float) rate);
        if (logoOptions.getLogo() != null && logoOptions.getLogo().getImg() != null
                && logoOptions.getLogo().getImg().getWidth() != logoOptions.getLogo().getImg().getHeight()) {
            // 针对logo非矩形的场景，进行适配
            float logoRate = logoOptions.getLogo().getImg().getWidth() / (float) logoOptions.getLogo().getImg().getHeight();
            if (logoRate > 1) {
                // 当宽 > 高时，需要缩小logo的高度范围
                logoHeight = (int) (logoHeight / logoRate);
            } else {
                // 当宽 < 高时，则需要缩小logo的宽度范围
                logoWidth = (int) (logoWidth * logoRate);
            }
        }
        int logoX = (width - logoWidth) / 2;
        int logoY = (height - logoHeight) / 2;
        for (int x = logoX; x <= logoX + logoWidth; x++) {
            for (int y = logoY; y <= logoY + logoHeight; y++) {
                bitMatrixEx.getByteMatrix().set(x, y, 0);
            }
        }
    }

    /**
     * 对 zxing 的 QRCodeWriter 进行扩展, 解决白边过多的问题
     * <p/>
     * 源码参考 {@link com.google.zxing.qrcode.QRCodeWriter#renderResult(QRCode, int, int, int)}
     *
     * @param code
     * @param width
     * @param height
     * @param quietZone 取值 [0, 4]
     * @return
     */
    private static BitMatrixEx renderResult(QRCode code, int width, int height, int quietZone) {
        ByteMatrix input = code.getMatrix();
        if (input == null) {
            throw new IllegalStateException();
        }

        // xxx 二维码宽高相等, 即 qrWidth == qrHeight
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        int qrWidth = inputWidth + (quietZone * 2);
        int qrHeight = inputHeight + (quietZone * 2);


        // 白边过多时, 缩放
        int minSize = Math.min(width, height);
        int scale = calculateScale(qrWidth, minSize);
        if (scale > 0) {
            // 计算边框留白
            int padding = (minSize - qrWidth * scale) / MAX_QUIET_ZONE_SIZE * quietZone;
            int tmpValue = qrWidth * scale + padding;
            if (width == height) {
                width = tmpValue;
                height = tmpValue;
            } else if (width > height) {
                width = width * tmpValue / height;
                height = tmpValue;
            } else {
                height = height * tmpValue / width;
                width = tmpValue;
            }
        }

        int outputWidth = Math.max(width, qrWidth);
        int outputHeight = Math.max(height, qrHeight);

        int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
        int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
        int topPadding = (outputHeight - (inputHeight * multiple)) / 2;


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
