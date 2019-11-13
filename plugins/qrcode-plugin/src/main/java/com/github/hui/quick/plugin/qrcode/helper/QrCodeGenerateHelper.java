package com.github.hui.quick.plugin.qrcode.helper;

import com.github.hui.quick.plugin.qrcode.wrapper.BitMatrixEx;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 二维码生成辅助类，主要两个方法，一个是生成二维码矩阵，一个是渲染矩阵为图片
 * Created by yihui on 2018/3/23.
 */
@Slf4j
public class QrCodeGenerateHelper {
    private static final int QUIET_ZONE_SIZE = 4;


    /**
     * 对 zxing 的 QRCodeWriter 进行扩展, 解决白边过多的问题
     * <p/>
     * 源码参考 {@link com.google.zxing.qrcode.QRCodeWriter#encode(String, BarcodeFormat, int, int, Map)}
     */
    public static BitMatrixEx encode(QrCodeOptions qrCodeConfig) throws WriterException {
        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
        int quietZone = 1;
        if (qrCodeConfig.getHints() != null) {
            if (qrCodeConfig.getHints().containsKey(EncodeHintType.ERROR_CORRECTION)) {
                errorCorrectionLevel = ErrorCorrectionLevel
                        .valueOf(qrCodeConfig.getHints().get(EncodeHintType.ERROR_CORRECTION).toString());
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
            if (log.isDebugEnabled()) {
                log.debug("qrCode scale enable! scale: {}, qrSize:{}, expectSize:{}x{}", scale, qrWidth, width, height);
            }

            int padding, tmpValue;
            // 计算边框留白
            padding = (minSize - qrWidth * scale) / QUIET_ZONE_SIZE * quietZone;
            tmpValue = qrWidth * scale + padding;
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


    /**
     * 根据二维码配置 & 二维码矩阵生成二维码图片
     *
     * @param qrCodeConfig
     * @param bitMatrix
     * @return
     * @throws IOException
     */
    public static BufferedImage toBufferedImage(QrCodeOptions qrCodeConfig, BitMatrixEx bitMatrix) throws IOException {
        int qrCodeWidth = bitMatrix.getWidth();
        int qrCodeHeight = bitMatrix.getHeight();
        BufferedImage qrCode = QrCodeRenderHelper.drawQrInfo(qrCodeConfig, bitMatrix);

        // 若二维码的实际宽高和预期的宽高不一致, 则缩放
        int realQrCodeWidth = qrCodeConfig.getW();
        int realQrCodeHeight = qrCodeConfig.getH();
        if (qrCodeWidth != realQrCodeWidth || qrCodeHeight != realQrCodeHeight) {
            BufferedImage tmp = new BufferedImage(realQrCodeWidth, realQrCodeHeight, BufferedImage.TYPE_INT_RGB);
            tmp.getGraphics()
                    .drawImage(qrCode.getScaledInstance(realQrCodeWidth, realQrCodeHeight, Image.SCALE_SMOOTH), 0, 0,
                            null);
            qrCode = tmp;
        }


        /**
         * 说明
         *  在覆盖模式下，先设置二维码的透明度，然后绘制在背景图的正中央，最后绘制logo，这样保证logo不会透明，显示清晰
         *  在填充模式下，先绘制logo，然后绘制在背景的指定位置上；若先绘制背景，再绘制logo，则logo大小偏移量的计算会有问题
         */
        boolean logoAlreadyDraw = false;
        // 绘制背景图
        if (qrCodeConfig.getBgImgOptions() != null) {
            if (qrCodeConfig.getBgImgOptions().getBgImgStyle() == QrCodeOptions.BgImgStyle.FILL &&
                    qrCodeConfig.getLogoOptions() != null) {
                // 此种模式，先绘制logo
                qrCode = QrCodeRenderHelper.drawLogo(qrCode, qrCodeConfig.getLogoOptions());
                logoAlreadyDraw = true;
            }

            qrCode = QrCodeRenderHelper.drawBackground(qrCode, qrCodeConfig.getBgImgOptions());
        }


        // 插入logo
        if (qrCodeConfig.getLogoOptions() != null && !logoAlreadyDraw) {
            qrCode = QrCodeRenderHelper.drawLogo(qrCode, qrCodeConfig.getLogoOptions());
        }

        return qrCode;
    }


    public static List<ImmutablePair<BufferedImage, Integer>> toGifImages(QrCodeOptions qrCodeConfig,
            BitMatrixEx bitMatrix) {
        if (qrCodeConfig.getBgImgOptions() == null ||
                qrCodeConfig.getBgImgOptions().getGifDecoder().getFrameCount() <= 0) {
            throw new IllegalArgumentException("animated background image should not be null!");
        }

        int qrCodeWidth = bitMatrix.getWidth();
        int qrCodeHeight = bitMatrix.getHeight();
        BufferedImage qrCode = QrCodeRenderHelper.drawQrInfo(qrCodeConfig, bitMatrix);

        // 若二维码的实际宽高和预期的宽高不一致, 则缩放
        int realQrCodeWidth = qrCodeConfig.getW();
        int realQrCodeHeight = qrCodeConfig.getH();
        if (qrCodeWidth != realQrCodeWidth || qrCodeHeight != realQrCodeHeight) {
            BufferedImage tmp = new BufferedImage(realQrCodeWidth, realQrCodeHeight, BufferedImage.TYPE_INT_RGB);
            tmp.getGraphics()
                    .drawImage(qrCode.getScaledInstance(realQrCodeWidth, realQrCodeHeight, Image.SCALE_SMOOTH), 0, 0,
                            null);
            qrCode = tmp;
        }

        boolean logoAlreadyDraw = false;
        if (qrCodeConfig.getBgImgOptions().getBgImgStyle() == QrCodeOptions.BgImgStyle.FILL &&
                qrCodeConfig.getLogoOptions() != null) {
            // 此种模式，先绘制logo
            qrCode = QrCodeRenderHelper.drawLogo(qrCode, qrCodeConfig.getLogoOptions());
            logoAlreadyDraw = true;
        }


        // 绘制动态背景图
        List<ImmutablePair<BufferedImage, Integer>> bgList =
                QrCodeRenderHelper.drawGifBackground(qrCode, qrCodeConfig.getBgImgOptions());

        // 插入logo

        if (qrCodeConfig.getLogoOptions() != null && !logoAlreadyDraw) {
            List<ImmutablePair<BufferedImage, Integer>> result = new ArrayList<>(bgList.size());
            for (ImmutablePair<BufferedImage, Integer> pair : bgList) {
                result.add(ImmutablePair.of(QrCodeRenderHelper.drawLogo(pair.getLeft(), qrCodeConfig.getLogoOptions()),
                        pair.getRight()));
            }
            return result;
        } else {
            return bgList;
        }
    }
}
