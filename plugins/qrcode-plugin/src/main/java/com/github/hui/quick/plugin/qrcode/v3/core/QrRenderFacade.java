package com.github.hui.quick.plugin.qrcode.v3.core;

import com.github.hui.quick.plugin.qrcode.v3.constants.BgStyle;
import com.github.hui.quick.plugin.qrcode.v3.core.calculate.QrRenderDotGenerator;
import com.github.hui.quick.plugin.qrcode.v3.core.matrix.QrMatrixGenerator;
import com.github.hui.quick.plugin.qrcode.v3.core.render.QrImgRender;
import com.github.hui.quick.plugin.qrcode.v3.core.render.QrSvgRender;
import com.github.hui.quick.plugin.qrcode.v3.core.render.QrTxtRender;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.RenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.svg.SvgTemplate;
import com.github.hui.quick.plugin.qrcode.v3.req.QrCodeV3Options;
import com.github.hui.quick.plugin.qrcode.wrapper.BitMatrixEx;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YiHui
 * @date 2022/8/2
 */
public class QrRenderFacade {
    public static BufferedImage renderAsImg(QrCodeV3Options options) throws Exception {
        BitMatrixEx matrix = QrMatrixGenerator.calculateMatrix(options);
        List<RenderDot> renderDotList = QrRenderDotGenerator.calculateRenderDots(options, matrix);

        // 绘制二维码
        BufferedImage qrImg = QrImgRender.drawQrInfo(renderDotList, options);

        //说明
        // 在覆盖模式下，先设置二维码的透明度，然后绘制在背景图的正中央，最后绘制logo，这样保证logo不会透明，显示清晰
        // 在填充模式下，先绘制logo，然后绘制在背景的指定位置上；若先绘制背景，再绘制logo，则logo大小偏移量的计算会有问题
        if (options.getBgOptions().getBgStyle() == BgStyle.FILL) {
            qrImg = QrImgRender.drawLogo(qrImg, options);
            qrImg = QrImgRender.drawBackground(qrImg, options.getBgOptions());
        } else {
            qrImg = QrImgRender.drawBackground(qrImg, options.getBgOptions());
            qrImg = QrImgRender.drawLogo(qrImg, options);
        }

        // 前置图
        qrImg = QrImgRender.drawFront(qrImg, options);

        // 如果指定了二维码输出图片样式，则需要额外处理一下
        if (options.getDrawOptions().getPicStyle() != null) {
            qrImg = options.getDrawOptions().getPicStyle().process(qrImg, options.getDrawOptions().getCornerRadius());
        }
        return qrImg;
    }

    public static List<ImmutablePair<BufferedImage, Integer>> renderAsGif(QrCodeV3Options options) throws Exception {
        boolean preGif = options.getFrontOptions() != null && options.getFrontOptions().getFt() != null && options.getFrontOptions().getFt().getGif().getFrameCount() > 0;
        boolean bgGif = options.getBgOptions() != null && options.getBgOptions().getBg() != null && options.getBgOptions().getBg().getGif().getFrameCount() > 0;
        if (!bgGif && !preGif) {
            throw new IllegalArgumentException("animated background image should not be null!");
        }

        BitMatrixEx matrix = QrMatrixGenerator.calculateMatrix(options);
        List<RenderDot> renderDotList = QrRenderDotGenerator.calculateRenderDots(options, matrix);

        // 绘制二维码
        BufferedImage qrImg = QrImgRender.drawQrInfo(renderDotList, options);

        boolean logoAlreadyDraw = false;

        if (options.getLogoOptions() != null && options.getBgOptions() != null && options.getBgOptions().getBgStyle() == BgStyle.FILL) {
            // 此种模式，先绘制logo
            qrImg = QrImgRender.drawLogo(qrImg, options);
            logoAlreadyDraw = true;
        }

        // 绘制动态背景图
        List<ImmutablePair<BufferedImage, Integer>> bgList = QrImgRender.drawGifBackground(qrImg, options.getBgOptions());
        if (!logoAlreadyDraw && options.getLogoOptions() != null && options.getLogoOptions().getLogo() != null) {
            if (!bgList.isEmpty()) {
                // 背景为gif时，为gif中的每一帧加上logo
                List<ImmutablePair<BufferedImage, Integer>> result = new ArrayList<>(bgList.size());
                for (ImmutablePair<BufferedImage, Integer> pair : bgList) {
                    result.add(ImmutablePair.of(QrImgRender.drawLogo(pair.getLeft(), options), pair.getRight()));
                }
                bgList = result;
            } else {
                // 背景非gif时，直接在qrImg上加上logo
                qrImg = QrImgRender.drawLogo(qrImg, options);
            }
        }

        if (!bgList.isEmpty()) {
            return bgList;
        }

        // todo 暂时不支持前置图和背景图同时设置为gif的场景，如果同时存在，只保存背景gif图

        // 前置图为gif的场景
        return QrImgRender.drawFrontGifImg(qrImg, options.getFrontOptions());
    }

    /**
     * 生成svg格式的二维码
     *
     * @param options
     * @return
     * @throws Exception
     */
    public static String renderAsSvg(QrCodeV3Options options) throws Exception {
        BitMatrixEx matrix = QrMatrixGenerator.calculateMatrix(options);
        List<RenderDot> renderDotList = QrRenderDotGenerator.calculateRenderDots(options, matrix);
        SvgTemplate svgTemplate = QrSvgRender.drawQrInfo(renderDotList, options);
        // 绘制logo
        QrSvgRender.drawLogo(svgTemplate, options);
        return svgTemplate.toString();
    }

    /**
     * 输出文字版二维码
     *
     * @param options
     * @return
     * @throws Exception
     */
    public static String renderAsTxt(QrCodeV3Options options) throws Exception {
        BitMatrixEx matrix = QrMatrixGenerator.calculateMatrix(options);
        return QrTxtRender.drawQrInfo(matrix, options).toString();
    }
}
