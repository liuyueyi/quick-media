package com.github.hui.quick.plugin.qrcode.v3;

import com.github.hui.quick.plugin.qrcode.helper.QrCodeRenderHelper;
import com.github.hui.quick.plugin.qrcode.wrapper.BitMatrixEx;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 二维码生成辅助类，主要两个方法，一个是生成二维码矩阵，一个是渲染矩阵为图片
 * Created by yihui on 2018/3/23.
 */
public class QrCodeGenerateV3Helper {

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
