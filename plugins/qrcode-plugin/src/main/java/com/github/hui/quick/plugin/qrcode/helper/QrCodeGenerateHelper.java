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
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 二维码生成辅助类，主要两个方法，一个是生成二维码矩阵，一个是渲染矩阵为图片
 * Created by yihui on 2018/3/23.
 */
public class QrCodeGenerateHelper {
    private static Logger log = LoggerFactory.getLogger(QrCodeGenerateHelper.class);
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
        BitMatrixEx bitMatrixEx = renderResult(code, qrCodeConfig.getW(), qrCodeConfig.getH(), quietZone);
        clearLogo(bitMatrixEx, qrCodeConfig.getLogoOptions());
        return bitMatrixEx;
    }

    private static void clearLogo(BitMatrixEx bitMatrixEx, QrCodeOptions.LogoOptions logoOptions) {
        if (logoOptions == null) {
            return;
        }

        // 将logo所占的点阵区间，全部设置为0，避免出现logo覆盖时，渲染处被部分覆盖的问题
        int rate = logoOptions.getRate() / 2;
        int width = bitMatrixEx.getByteMatrix().getWidth();
        int height = bitMatrixEx.getByteMatrix().getHeight();
        int logoWidth = (int) Math.ceil(width / (float)rate);
        int logoHeight = (int) Math.ceil(height / (float)rate);
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
        BufferedImage qrCode = QrCodeRenderHelper.drawQrInfo(qrCodeConfig, bitMatrix);

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

        // 前置图绘制
        if (qrCodeConfig.getFtImgOptions() != null) {
            qrCode = QrCodeRenderHelper.drawFrontImg(qrCode, qrCodeConfig.getFtImgOptions());
        }

        return qrCode;
    }


    public static List<ImmutablePair<BufferedImage, Integer>> toGifImages(QrCodeOptions qrCodeConfig,
                                                                          BitMatrixEx bitMatrix) {
        boolean preGif = qrCodeConfig.getFtImgOptions() != null && qrCodeConfig.getFtImgOptions().getGifDecoder().getFrameCount() > 0;
        boolean bgGif = qrCodeConfig.getBgImgOptions() != null && qrCodeConfig.getBgImgOptions().getGifDecoder().getFrameCount() > 0;
        if (!bgGif && !preGif) {
            throw new IllegalArgumentException("animated background image should not be null!");
        }

        BufferedImage qrCode = QrCodeRenderHelper.drawQrInfo(qrCodeConfig, bitMatrix);

        boolean logoAlreadyDraw = false;
        if (qrCodeConfig.getLogoOptions() != null && qrCodeConfig.getBgImgOptions() != null &&
                qrCodeConfig.getBgImgOptions().getBgImgStyle() == QrCodeOptions.BgImgStyle.FILL) {
            // 此种模式，先绘制logo
            qrCode = QrCodeRenderHelper.drawLogo(qrCode, qrCodeConfig.getLogoOptions());
            logoAlreadyDraw = true;
        }

        // 绘制动态背景图
        List<ImmutablePair<BufferedImage, Integer>> bgList = bgGif ?
                QrCodeRenderHelper.drawGifBackground(qrCode, qrCodeConfig.getBgImgOptions()) : Collections.emptyList();


        if (!logoAlreadyDraw && qrCodeConfig.getLogoOptions() != null) {
            if (bgGif) {
                // 插入logo
                List<ImmutablePair<BufferedImage, Integer>> result = new ArrayList<>(bgList.size());
                for (ImmutablePair<BufferedImage, Integer> pair : bgList) {
                    result.add(ImmutablePair.of(QrCodeRenderHelper.drawLogo(pair.getLeft(), qrCodeConfig.getLogoOptions()),
                            pair.getRight()));
                }
                bgList = result;
            } else {
                qrCode = QrCodeRenderHelper.drawLogo(qrCode, qrCodeConfig.getLogoOptions());
            }
        }

        if (bgGif) {
            return bgList;
        }

        // todo 暂时不支持前置图和背景图同时设置为gif的场景，如果同时存在，只保存背景gif图

        // 前置图为gif的场景
        return QrCodeRenderHelper.drawFrontGifImg(qrCode, qrCodeConfig.getFtImgOptions());
    }
}
