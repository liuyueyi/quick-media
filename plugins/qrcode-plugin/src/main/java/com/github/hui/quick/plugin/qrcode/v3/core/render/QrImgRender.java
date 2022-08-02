package com.github.hui.quick.plugin.qrcode.v3.core.render;

import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import com.github.hui.quick.plugin.base.awt.ImageOperateUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.BgStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.PicStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.RenderDotType;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.DetectRenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.RenderDot;
import com.github.hui.quick.plugin.qrcode.v3.req.BgOptions;
import com.github.hui.quick.plugin.qrcode.v3.req.FrontOptions;
import com.github.hui.quick.plugin.qrcode.v3.req.LogoOptions;
import com.github.hui.quick.plugin.qrcode.v3.req.QrCodeV3Options;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author YiHui
 * @date 2022/8/2
 */
public class QrImgRender {

    /**
     * 渲染qrInfo
     *
     * @param dotList
     * @param options
     */
    public static BufferedImage drawQrInfo(List<RenderDot> dotList, QrCodeV3Options options) {
        BufferedImage qrImg = GraphicUtil.createImg(options.getW(), options.getH(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = GraphicUtil.getG2d(qrImg);
        if (!options.getDrawOptions().isTransparencyFill()) {
            // 当二维码中的透明区域，不填充时，如下设置，可以让图片中的透明度覆盖背景色
            g2d.setComposite(AlphaComposite.Src);
        }

        // 直接背景铺满整个图
        g2d.setColor(options.getDrawOptions().getBgColor());
        g2d.fillRect(0, 0, options.getW(), options.getH());

        dotList.forEach(dot -> {
            if (dot.getType() == RenderDotType.DETECT.getType()) {
                // 探测图形
                DetectRenderDot dDot = (DetectRenderDot) dot;
                if (BooleanUtils.isTrue(dDot.getOutBorder())) {
                    g2d.setColor(options.getDetectOptions().getOutColor());
                } else {
                    g2d.setColor(options.getDetectOptions().getInColor());
                }
            } else if (dot.getType() == RenderDotType.BG.getType()) {
                g2d.setColor(options.getDrawOptions().getBgColor());
            } else {
                g2d.setColor(options.getDrawOptions().getPreColor());
            }

            // 执行渲染
            options.getDrawOptions().getDrawStyle().drawAsImg(g2d, dot);
        });
        g2d.dispose();
        return qrImg;
    }


    /**
     * 背景图
     */
    public static BufferedImage drawBackground(BufferedImage qrImg, BgOptions bgImgOptions) {
        if (bgImgOptions.getBg() == null) return qrImg;

        final int qrWidth = qrImg.getWidth();
        final int qrHeight = qrImg.getHeight();

        // 背景的图宽高不应该小于原图
        int bgW = Math.max(bgImgOptions.getBgW(), qrWidth);
        int bgH = Math.max(bgImgOptions.getBgH(), qrHeight);

        // 背景图缩放
        BufferedImage bgImg = bgImgOptions.getBg().getImg();
        if (bgImg.getWidth() != bgW || bgImg.getHeight() != bgH) {
            BufferedImage temp = new BufferedImage(bgW, bgH, BufferedImage.TYPE_INT_ARGB);
            temp.getGraphics().drawImage(bgImg.getScaledInstance(bgW, bgH, Image.SCALE_SMOOTH), 0, 0, null);
            bgImg = temp;
        }
        bgImg = bgImgOptions.getImgStyle().process(bgImg, (int) (Math.min(bgW, bgH) * bgImgOptions.getRadiusRate()));

        Graphics2D bgImgGraphic = GraphicUtil.getG2d(bgImg);
        if (bgImgOptions.getBgStyle() == BgStyle.FILL) {
            // 选择一块区域进行填充
            bgImgGraphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
            bgImgGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            bgImgGraphic.drawImage(qrImg.getScaledInstance(qrWidth, qrHeight, Image.SCALE_SMOOTH), bgImgOptions.getStartX(),
                    bgImgOptions.getStartY(), null);
        } else {
            // 全覆盖方式
            int bgOffsetX = (bgW - qrWidth) >> 1;
            int bgOffsetY = (bgH - qrHeight) >> 1;
            // 设置透明度， 避免看不到背景
            bgImgGraphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, bgImgOptions.getOpacity()));
            bgImgGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            bgImgGraphic.drawImage(qrImg.getScaledInstance(qrWidth, qrHeight, Image.SCALE_SMOOTH), bgOffsetX, bgOffsetY,
                    null);
            bgImgGraphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
        }
        bgImgGraphic.dispose();
        bgImg.flush();
        return bgImg;
    }

    public static List<ImmutablePair<BufferedImage, Integer>> drawGifBackground(BufferedImage qrImg, BgOptions bgImgOptions) {
        if (bgImgOptions.getBg() == null || bgImgOptions.getBg().getGifDecoder() == null) {
            return Collections.emptyList();
        }

        final int qrWidth = qrImg.getWidth();
        final int qrHeight = qrImg.getHeight();

        // 背景的图宽高不应该小于原图
        int bgW = Math.max(bgImgOptions.getBgW(), qrWidth);
        int bgH = Math.max(bgImgOptions.getBgH(), qrHeight);

        // 覆盖方式
        boolean fillMode = bgImgOptions.getBgStyle() == BgStyle.FILL;
        int bgOffsetX = fillMode ? bgImgOptions.getStartX() : (bgW - qrWidth) >> 1;
        int bgOffsetY = fillMode ? bgImgOptions.getStartY() : (bgH - qrHeight) >> 1;

        int gifImgLen = bgImgOptions.getBg().getGifDecoder().getFrameCount();
        List<ImmutablePair<BufferedImage, Integer>> result = new ArrayList<>(gifImgLen);
        // 背景图缩放
        for (int index = 0, len = bgImgOptions.getBg().getGifDecoder().getFrameCount(); index < len; index++) {
            BufferedImage bgImg = bgImgOptions.getBg().getGifDecoder().getFrame(index);
            // fixme 当背景图为png时，最终透明的地方会是黑色，这里兼容处理成白色
            BufferedImage temp = new BufferedImage(bgW, bgH, BufferedImage.TYPE_INT_RGB);
            temp.getGraphics().setColor(Color.WHITE);
            temp.getGraphics().fillRect(0, 0, bgW, bgH);
            temp.getGraphics().drawImage(bgImg.getScaledInstance(bgW, bgH, Image.SCALE_SMOOTH), 0, 0, null);
            bgImg = temp;

            Graphics2D bgGraphic = GraphicUtil.getG2d(bgImg);
            if (fillMode) {
                // 选择一块区域进行填充
                bgGraphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
                bgGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                bgGraphic.drawImage(qrImg.getScaledInstance(qrWidth, qrHeight, Image.SCALE_SMOOTH), bgOffsetX, bgOffsetY, null);
//                // 实验功能，用于gif生成时缩放
//                int add = 2 * Math.abs(index - len / 2);
//                int newQrW = qrWidth + add;
//                int newQrH = qrHeight + add;
//
//                bgGraphic.drawImage(qrImg.getScaledInstance(newQrW, newQrH, Image.SCALE_SMOOTH), bgOffsetX - add / 2, bgOffsetY - add / 2, null);
            } else {
                // 全覆盖模式, 设置透明度，避免看不到背景
                bgGraphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, bgImgOptions.getOpacity()));
                bgGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                bgGraphic.drawImage(qrImg.getScaledInstance(qrWidth, qrHeight, Image.SCALE_SMOOTH), bgOffsetX, bgOffsetY, null);
                bgGraphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
            }
            bgGraphic.dispose();
            bgImg.flush();

            result.add(ImmutablePair.of(bgImg, bgImgOptions.getBg().getGifDecoder().getDelay(index)));
        }
        return result;
    }


    /**
     * 前置装饰
     */
    public static BufferedImage drawFront(BufferedImage qrImg, FrontOptions frontImgOptions) {
        if (frontImgOptions.getFt() == null) return qrImg;

        int resW = Math.max(frontImgOptions.getFtW(), qrImg.getWidth());
        int resH = Math.max(frontImgOptions.getFtH(), qrImg.getHeight());

        // 先将二维码绘制在底层背景图上
        int startX = frontImgOptions.getStartX(), startY = frontImgOptions.getStartY();
        BufferedImage bottomImg = GraphicUtil.createImg(resW, resH, Math.max(startX, 0),
                Math.max(startY, 0), qrImg, frontImgOptions.getFillColor());


        BufferedImage ftImg = frontImgOptions.getFt().getImg();
        // 前置图支持设置圆角 or 圆形设置
        ftImg = frontImgOptions.getImgStyle().process(ftImg, (int) (Math.min(resW, resH) * frontImgOptions.getRadius()));

        Graphics2D g2d = GraphicUtil.getG2d(bottomImg);
        boolean needScale = frontImgOptions.getFtW() < ftImg.getWidth() || frontImgOptions.getFtH() < ftImg.getHeight();
        g2d.drawImage(!needScale ? ftImg : ftImg.getScaledInstance(frontImgOptions.getFtW(), frontImgOptions.getFtH(), BufferedImage.SCALE_SMOOTH),
                -Math.min(startX, 0),
                -Math.min(startY, 0),
                null);
        g2d.dispose();
        return bottomImg;
    }

    public static List<ImmutablePair<BufferedImage, Integer>> drawFrontGifImg(BufferedImage qrImg, FrontOptions frontImgOptions) {
        final int qrWidth = qrImg.getWidth();
        final int qrHeight = qrImg.getHeight();

        int ftW = frontImgOptions.getFtW();
        int ftH = frontImgOptions.getFtH();

        int resW = Math.max(ftW, qrWidth);
        int resH = Math.max(ftH, qrHeight);

        // 先将二维码绘制在底层背景图上
        int startX = frontImgOptions.getStartX(), startY = frontImgOptions.getStartY();
        BufferedImage bottomImg = GraphicUtil.createImg(resW, resH, Math.max(startX, 0),
                Math.max(startY, 0), qrImg, frontImgOptions.getFillColor());


        int gifImgLen = frontImgOptions.getFt().getGifDecoder().getFrameCount();
        List<ImmutablePair<BufferedImage, Integer>> result = new ArrayList<>(gifImgLen);
        // 背景图缩放
        BufferedImage ftImg = frontImgOptions.getFt().getGifDecoder().getFrame(0);
        boolean needScale = frontImgOptions.getFtW() < ftImg.getWidth() || frontImgOptions.getFtH() < ftImg.getHeight();
        for (int index = 0; index < gifImgLen; index++) {
            BufferedImage bgImg = GraphicUtil.createImg(resW, resH, bottomImg);
            Graphics2D bgGraphic = GraphicUtil.getG2d(bgImg);
            ftImg = frontImgOptions.getFt().getGifDecoder().getFrame(index);
            bgGraphic.drawImage(!needScale ? ftImg : ftImg.getScaledInstance(frontImgOptions.getFtW(), frontImgOptions.getFtH(), BufferedImage.SCALE_SMOOTH),
                    -Math.min(startX, 0), -Math.min(startY, 0), null);

            bgGraphic.dispose();
            bgImg.flush();

            result.add(ImmutablePair.of(bgImg, frontImgOptions.getFt().getGifDecoder().getDelay(index)));
        }
        return result;
    }


    /**
     * logo
     */
    public static BufferedImage drawLogo(BufferedImage qrImg, LogoOptions logoOptions) {
        if (logoOptions.getLogo() == null) return qrImg;

        final int qrWidth = qrImg.getWidth();
        final int qrHeight = qrImg.getHeight();

        // 获取logo图片
        BufferedImage logoImg = logoOptions.getLogo().getImg();

        // 默认不处理logo
        int radius = 0;
        if (logoOptions.getPicStyle() == PicStyle.ROUND) {
            // 绘制圆角图片
            radius = logoImg.getWidth() >> 2;
            logoImg = ImageOperateUtil.makeRoundedCorner(logoImg, radius);
        } else if (logoOptions.getPicStyle() == PicStyle.CIRCLE) {
            // 绘制圆形logo
            radius = Math.min(logoImg.getWidth(), logoImg.getHeight());
            logoImg = ImageOperateUtil.makeRoundImg(logoImg, false, null);
        }

        // 绘制边框
        if (logoOptions.getBorderColor() != null) {
            if (logoOptions.getOuterBorderColor() != null) {
                // 两层边框
                logoImg = ImageOperateUtil.makeRoundBorder(logoImg, radius, logoOptions.getOuterBorderColor());
            }

            logoImg = ImageOperateUtil.makeRoundBorder(logoImg, radius, logoOptions.getBorderColor());
        }

        // logo的宽高，避免长图的变形，这里采用等比例缩放的策略
        int logoRate = logoOptions.getRate();
        int calculateQrLogoWidth = (qrWidth << 1) / logoRate;
        int calculateQrLogoHeight = (qrHeight << 1) / logoRate;
        int logoWidth, logoHeight;
        if (calculateQrLogoWidth < logoImg.getWidth()) {
            // logo实际宽大于计算的宽度，则需要等比例缩放
            logoWidth = calculateQrLogoWidth;
            logoHeight = logoWidth * logoImg.getHeight() / logoImg.getWidth();
        } else if (calculateQrLogoHeight < logoImg.getHeight()) {
            // logo实际高大于计算的高度，则需要等比例缩放
            logoHeight = calculateQrLogoHeight;
            logoWidth = logoHeight * logoImg.getWidth() / logoImg.getHeight();
        } else {
            // logo 宽高比计算的要小，不拉伸
            logoWidth = logoImg.getWidth();
            logoHeight = logoImg.getHeight();
        }
        int logoOffsetX = (qrWidth - logoWidth) >> 1;
        int logoOffsetY = (qrHeight - logoHeight) >> 1;


        // 插入LOGO
        Graphics2D qrImgGraphic = GraphicUtil.getG2d(qrImg);

        if (logoOptions.getOpacity() != null) {
            qrImgGraphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, logoOptions.getOpacity()));
        }
        qrImgGraphic.drawImage(logoImg.getScaledInstance(logoWidth, logoHeight, BufferedImage.SCALE_SMOOTH), logoOffsetX, logoOffsetY, null);
        qrImgGraphic.dispose();
        logoImg.flush();
        return qrImg;
    }
}
