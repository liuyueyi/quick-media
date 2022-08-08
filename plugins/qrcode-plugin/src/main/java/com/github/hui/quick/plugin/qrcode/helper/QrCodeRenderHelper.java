package com.github.hui.quick.plugin.qrcode.helper;

import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import com.github.hui.quick.plugin.base.awt.ImageOperateUtil;
import com.github.hui.quick.plugin.qrcode.constants.QuickQrUtil;
import com.github.hui.quick.plugin.qrcode.entity.DotSize;
import com.github.hui.quick.plugin.qrcode.entity.RenderImgResourcesV2;
import com.github.hui.quick.plugin.qrcode.helper.v2.ImgRenderV2Helper;
import com.github.hui.quick.plugin.qrcode.wrapper.BitMatrixEx;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 二维码渲染辅助类，主要用于绘制背景，logo，定位点，二维码信息
 * Created by yihui on 2017/4/7.
 */
public class QrCodeRenderHelper {
    /**
     * 绘制logo图片
     *
     * @param qrImg
     * @param logoOptions
     * @return
     */
    public static BufferedImage drawLogo(BufferedImage qrImg, QrCodeOptions.LogoOptions logoOptions) {
        final int qrWidth = qrImg.getWidth();
        final int qrHeight = qrImg.getHeight();

        // 获取logo图片
        BufferedImage logoImg = logoOptions.getLogo();


        // 默认不处理logo
        int radius = 0;
        if (logoOptions.getLogoStyle() == QrCodeOptions.LogoStyle.ROUND) {
            // 绘制圆角图片
            radius = logoImg.getWidth() >> 2;
            logoImg = ImageOperateUtil.makeRoundedCorner(logoImg, radius);
        } else if (logoOptions.getLogoStyle() == QrCodeOptions.LogoStyle.CIRCLE) {
            // 绘制圆形logo
            radius = Math.min(logoImg.getWidth(), logoImg.getHeight());
            logoImg = ImageOperateUtil.makeRoundImg(logoImg, false, null);
        }

        // 绘制边框
        if (logoOptions.isBorder()) {
            if (logoOptions.getOuterBorderColor() != null) {
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
        qrImgGraphic
                .drawImage(logoImg.getScaledInstance(logoWidth, logoHeight, BufferedImage.SCALE_SMOOTH), logoOffsetX,
                        logoOffsetY, null);
        qrImgGraphic.dispose();
        logoImg.flush();
        return qrImg;
    }

    /**
     * 绘制前置图
     *
     * @param qrImg
     * @param frontImgOptions
     * @return
     */
    public static BufferedImage drawFrontImg(BufferedImage qrImg, QrCodeOptions.FrontImgOptions frontImgOptions) {
        final int qrWidth = qrImg.getWidth();
        final int qrHeight = qrImg.getHeight();

        int resW = Math.max(frontImgOptions.getFtW(), qrWidth);
        int resH = Math.max(frontImgOptions.getFtH(), qrHeight);

        // 先将二维码绘制在底层背景图上
        int startX = frontImgOptions.getStartX(), startY = frontImgOptions.getStartY();
        BufferedImage bottomImg = GraphicUtil.createImg(resW, resH, Math.max(startX, 0),
                Math.max(startY, 0), qrImg, frontImgOptions.getFillColor());


        BufferedImage ftImg = frontImgOptions.getFtImg();
        // 前置图支持设置圆角 or 圆形设置
        if (frontImgOptions.getImgStyle() == QrCodeOptions.ImgStyle.ROUND) {
            int cornerRadius = (int) (Math.min(resW, resH) * frontImgOptions.getRadius());
            ftImg = ImageOperateUtil.makeRoundedCorner(ftImg, cornerRadius);
        } else if (frontImgOptions.getImgStyle() == QrCodeOptions.ImgStyle.CIRCLE) {
            ftImg = ImageOperateUtil.makeRoundImg(ftImg, false, null);
        }

        Graphics2D g2d = GraphicUtil.getG2d(bottomImg);
        boolean needScale = frontImgOptions.getFtW() < ftImg.getWidth() || frontImgOptions.getFtH() < ftImg.getHeight();
        g2d.drawImage(!needScale ? ftImg : ftImg.getScaledInstance(frontImgOptions.getFtW(), frontImgOptions.getFtH(), BufferedImage.SCALE_SMOOTH),
                -Math.min(startX, 0), -Math.min(startY, 0), null);
        g2d.dispose();
        return bottomImg;
    }

    public static List<ImmutablePair<BufferedImage, Integer>> drawFrontGifImg(BufferedImage qrImg,
                                                                              QrCodeOptions.FrontImgOptions frontImgOptions) {
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


        int gifImgLen = frontImgOptions.getGifDecoder().getFrameCount();
        List<ImmutablePair<BufferedImage, Integer>> result = new ArrayList<>(gifImgLen);
        // 背景图缩放
        BufferedImage ftImg = frontImgOptions.getGifDecoder().getFrame(0);
        boolean needScale = frontImgOptions.getFtW() < ftImg.getWidth() || frontImgOptions.getFtH() < ftImg.getHeight();
        for (int index = 0; index < gifImgLen; index++) {
            BufferedImage bgImg = GraphicUtil.createImg(resW, resH, bottomImg);
            Graphics2D bgGraphic = GraphicUtil.getG2d(bgImg);
            ftImg = frontImgOptions.getGifDecoder().getFrame(index);
            bgGraphic.drawImage(!needScale ? ftImg : ftImg.getScaledInstance(frontImgOptions.getFtW(), frontImgOptions.getFtH(), BufferedImage.SCALE_SMOOTH),
                    -Math.min(startX, 0), -Math.min(startY, 0), null);

            bgGraphic.dispose();
            bgImg.flush();

            result.add(ImmutablePair.of(bgImg, frontImgOptions.getGifDecoder().getDelay(index)));
        }
        return result;
    }


    /**
     * 绘制背景图
     *
     * @param qrImg        二维码图
     * @param bgImgOptions 背景图信息
     * @return
     */
    public static BufferedImage drawBackground(BufferedImage qrImg, QrCodeOptions.BgImgOptions bgImgOptions) {
        final int qrWidth = qrImg.getWidth();
        final int qrHeight = qrImg.getHeight();

        // 背景的图宽高不应该小于原图
        int bgW = Math.max(bgImgOptions.getBgW(), qrWidth);
        int bgH = Math.max(bgImgOptions.getBgH(), qrHeight);

        // 背景图缩放
        BufferedImage bgImg = bgImgOptions.getBgImg();
        if (bgImg.getWidth() != bgW || bgImg.getHeight() != bgH) {
            BufferedImage temp = new BufferedImage(bgW, bgH, BufferedImage.TYPE_INT_ARGB);
            temp.getGraphics().drawImage(bgImg.getScaledInstance(bgW, bgH, Image.SCALE_SMOOTH), 0, 0, null);
            bgImg = temp;
        }

        // 背景图支持设置圆角 or 圆形设置
        if (bgImgOptions.getImgStyle() == QrCodeOptions.ImgStyle.ROUND) {
            int cornerRadius = (int) (Math.min(bgW, bgH) * bgImgOptions.getRadius());
            bgImg = ImageOperateUtil.makeRoundedCorner(bgImg, cornerRadius);
        } else if (bgImgOptions.getImgStyle() == QrCodeOptions.ImgStyle.CIRCLE) {
            bgImg = ImageOperateUtil.makeRoundImg(bgImg, false, null);
        }

        Graphics2D bgImgGraphic = GraphicUtil.getG2d(bgImg);
        if (bgImgOptions.getBgImgStyle() == QrCodeOptions.BgImgStyle.FILL) {
            // 选择一块区域进行填充
            bgImgGraphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
            bgImgGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            bgImgGraphic
                    .drawImage(qrImg.getScaledInstance(qrWidth, qrHeight, Image.SCALE_SMOOTH), bgImgOptions.getStartX(),
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


    /**
     * 动态背景图绘制
     *
     * @param qrImg
     * @param bgImgOptions
     * @return
     */
    public static List<ImmutablePair<BufferedImage, Integer>> drawGifBackground(BufferedImage qrImg,
                                                                                QrCodeOptions.BgImgOptions bgImgOptions) {
        final int qrWidth = qrImg.getWidth();
        final int qrHeight = qrImg.getHeight();

        // 背景的图宽高不应该小于原图
        int bgW = Math.max(bgImgOptions.getBgW(), qrWidth);
        int bgH = Math.max(bgImgOptions.getBgH(), qrHeight);

        // 覆盖方式
        boolean fillMode = bgImgOptions.getBgImgStyle() == QrCodeOptions.BgImgStyle.FILL;
        int bgOffsetX = fillMode ? bgImgOptions.getStartX() : (bgW - qrWidth) >> 1;
        int bgOffsetY = fillMode ? bgImgOptions.getStartY() : (bgH - qrHeight) >> 1;

        int gifImgLen = bgImgOptions.getGifDecoder().getFrameCount();
        List<ImmutablePair<BufferedImage, Integer>> result = new ArrayList<>(gifImgLen);
        // 背景图缩放
        for (int index = 0, len = bgImgOptions.getGifDecoder().getFrameCount(); index < len; index++) {
            BufferedImage bgImg = bgImgOptions.getGifDecoder().getFrame(index);
            // fixme 当背景图为png时，最终透明的地方会是黑色，这里兼容处理成白色
            BufferedImage temp = new BufferedImage(bgW, bgH, BufferedImage.TYPE_INT_RGB);
            temp.getGraphics().setColor(Color.WHITE);
            temp.getGraphics().fillRect(0, 0, bgW, bgH);
            temp.getGraphics().drawImage(bgImg.getScaledInstance(bgW, bgH, Image.SCALE_SMOOTH), 0, 0, null);
            bgImg = temp;

            Graphics2D bgGraphic = GraphicUtil.getG2d(bgImg);
            if (bgImgOptions.getBgImgStyle() == QrCodeOptions.BgImgStyle.FILL) {
                // 选择一块区域进行填充
                bgGraphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
                bgGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                bgGraphic
//                        .drawImage(qrImg.getScaledInstance(qrWidth, qrHeight, Image.SCALE_SMOOTH), bgOffsetX, bgOffsetY,
//                                null);
                // 实验功能，用于gif生成时缩放
                int add = 2 * Math.abs(index - len / 2);
                int newQrW = qrWidth + add;
                int newQrH = qrHeight + add;

                bgGraphic.drawImage(qrImg.getScaledInstance(newQrW, newQrH, Image.SCALE_SMOOTH), bgOffsetX - add / 2, bgOffsetY - add / 2, null);
            } else {
                // 全覆盖模式, 设置透明度， 避免看不到背景
                bgGraphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, bgImgOptions.getOpacity()));
                bgGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                bgGraphic
                        .drawImage(qrImg.getScaledInstance(qrWidth, qrHeight, Image.SCALE_SMOOTH), bgOffsetX, bgOffsetY,
                                null);
                bgGraphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
            }
            bgGraphic.dispose();
            bgImg.flush();

            result.add(ImmutablePair.of(bgImg, bgImgOptions.getGifDecoder().getDelay(index)));
        }
        return result;
    }


    /**
     * 根据二维码矩阵，生成对应的二维码推片
     *
     * @param qrCodeConfig
     * @param bitMatrix
     * @return
     */
    public static BufferedImage drawQrInfo(QrCodeOptions qrCodeConfig, BitMatrixEx bitMatrix) {
        int qrWidth = bitMatrix.getWidth();
        int qrHeight = bitMatrix.getHeight();
        int infoSize = bitMatrix.getMultiple();
        BufferedImage qrImg = new BufferedImage(qrWidth, qrHeight, BufferedImage.TYPE_INT_ARGB);

        QrCodeOptions.DrawOptions drawOptions = qrCodeConfig.getDrawOptions();

        // 绘制的背景色
        Color bgColor = drawOptions.getBgColor();
        // 绘制前置色
        Color preColor = drawOptions.getPreColor();

        // 探测图形外圈的颜色
        Color detectOutColor = qrCodeConfig.getDetectOptions().getOutColor();
        // 探测图形内圈的颜色
        Color detectInnerColor = qrCodeConfig.getDetectOptions().getInColor();

        if (detectInnerColor != null || detectOutColor != null) {
            if (detectInnerColor == null) {
                detectInnerColor = detectOutColor;
            } else if (detectOutColor == null) {
                detectOutColor = detectInnerColor;
            }
        }


        int leftPadding = bitMatrix.getLeftPadding();
        int topPadding = bitMatrix.getTopPadding();

        Graphics2D g2 = GraphicUtil.getG2d(qrImg);
        if (!drawOptions.isDiaphaneityFill()) {
            // 当二维码中的透明区域，不填充时，如下设置，可以让图片中的透明度覆盖背景色
            g2.setComposite(AlphaComposite.Src);
        }

        // 直接背景铺满整个图
        g2.setColor(bgColor);
        g2.fillRect(0, 0, qrWidth, qrHeight);

        if (drawOptions.getDrawStyle() == QrCodeOptions.DrawStyle.TXT) {
            // 绘制文字时，需要设置字体
            g2.setFont(QuickQrUtil.font(drawOptions.getFontName(), drawOptions.getFontStyle(), infoSize));
        }

        // 探测图形的大小
        int detectCornerSize = bitMatrix.getByteMatrix().get(0, 5) == 1 ? 7 : 5;

        int matrixW = bitMatrix.getByteMatrix().getWidth();
        int matrixH = bitMatrix.getByteMatrix().getHeight();

        QrCodeOptions.DrawStyle drawStyle = drawOptions.getDrawStyle();
        if (drawStyle == QrCodeOptions.DrawStyle.IMAGE_V2) {
            // 若探测图形特殊绘制，则提前处理掉
            RenderImgResourcesV2 imgResourcesV2 = drawOptions.getImgResourcesForV2();
            for (int x = 0; x < matrixW; x++) {
                for (int y = 0; y < matrixH; y++) {
                    DetectLocation detectLocation = inDetectCornerArea(x, y, matrixW, matrixH, detectCornerSize);
                    if (detectLocation.detectedArea()) {
                        // 若探测图形特殊绘制，则单独处理
                        if (qrCodeConfig.getDetectOptions().getSpecial()) {
                            if (bitMatrix.getByteMatrix().get(x, y) == 1) {
                                // 绘制三个位置探测图形
                                drawDetectImg(qrCodeConfig, g2, bitMatrix, matrixW, matrixH, leftPadding, topPadding, infoSize,
                                        detectCornerSize, x, y, detectOutColor, detectInnerColor, detectLocation);
                                bitMatrix.getByteMatrix().set(x, y, 0);
                            }
                        } else {
                            if (bitMatrix.getByteMatrix().get(x, y) == 0 && imgResourcesV2.getDefaultBgImg() != null) {
                                QrCodeOptions.DrawStyle.IMAGE_V2.draw(g2, leftPadding + x * infoSize,
                                        topPadding + y * infoSize, infoSize, infoSize,
                                        imgResourcesV2.getDefaultBgImg(), null);
                            }
                        }
                        continue;
                    }

                    // 非探测区域内的0点图渲染
                    if (bitMatrix.getByteMatrix().get(x, y) == 0 && imgResourcesV2.getDefaultBgImg() != null) {
                        QrCodeOptions.DrawStyle.IMAGE_V2.draw(g2, leftPadding + x * infoSize,
                                topPadding + y * infoSize, infoSize, infoSize,
                                imgResourcesV2.getDefaultBgImg(), null);
                    }
                }
            }
            ImgRenderV2Helper.drawImg(g2, bitMatrix.getByteMatrix(), drawOptions.getImgResourcesForV2(), leftPadding, topPadding, infoSize);
        } else {
            DetectLocation detectLocation;
            for (int x = 0; x < matrixW; x++) {
                for (int y = 0; y < matrixH; y++) {
                    detectLocation = inDetectCornerArea(x, y, matrixW, matrixH, detectCornerSize);
                    if (bitMatrix.getByteMatrix().get(x, y) == 0) {
                        // 探测图形内部的元素与二维码的01点图绘制逻辑分开
                        // 绘制二维码中不在探测图形内部的0点图
                        if (!detectLocation.detectedArea() || !qrCodeConfig.getDetectOptions().getSpecial()) {
                            drawQrDotBgImg(qrCodeConfig, g2, leftPadding, topPadding, infoSize, x, y);
                        }
                        continue;
                    }

                    if (detectLocation.detectedArea() && qrCodeConfig.getDetectOptions().getSpecial()) {
                        // 绘制三个位置探测图形
                        drawDetectImg(qrCodeConfig, g2, bitMatrix, matrixW, matrixH, leftPadding, topPadding, infoSize,
                                detectCornerSize, x, y, detectOutColor, detectInnerColor, detectLocation);
                    } else {
                        g2.setColor(preColor);
                        // 绘制二维码的1点图
                        drawQrDotImg(qrCodeConfig, drawStyle, g2, bitMatrix, leftPadding, topPadding, infoSize, x, y);
                    }
                }
            }
        }
        g2.dispose();

        // 将二维码缩放为预期的大小
        qrImg = scaleQr2RealSize(qrCodeConfig, bitMatrix, qrImg);

        // 二维码图片样式调整
        if (drawOptions.getQrStyle() == QrCodeOptions.ImgStyle.CIRCLE) {
            return ImageOperateUtil.makeRoundImg(qrImg, false, null);
        } else if (drawOptions.getQrStyle() == QrCodeOptions.ImgStyle.ROUND) {
            float radius = Math.min(qrCodeConfig.getW(), qrCodeConfig.getH()) * drawOptions.getCornerRadius();
            return ImageOperateUtil.makeRoundedCorner(qrImg, (int) radius);
        } else {
            return qrImg;
        }
    }

    private static BufferedImage scaleQr2RealSize(QrCodeOptions qrCodeConfig, BitMatrixEx bitMatrix,
                                                  BufferedImage qrCode) {
        // 矩阵对应的宽高
        int qrCodeWidth = bitMatrix.getWidth();
        int qrCodeHeight = bitMatrix.getHeight();

        // 若二维码的实际宽高和预期的宽高不一致, 则缩放
        int realQrCodeWidth = qrCodeConfig.getW();
        int realQrCodeHeight = qrCodeConfig.getH();
        if (qrCodeWidth != realQrCodeWidth || qrCodeHeight != realQrCodeHeight) {
            qrCode = GraphicUtil.scaleImg(realQrCodeWidth, realQrCodeHeight, qrCode);
        }

        return qrCode;
    }

    public enum DetectLocation {
        /**
         * 左上角
         */
        LT,
        /**
         * 左下角
         */
        LD,
        /**
         * 右上角
         */
        RT,
        NONE {
            @Override
            public boolean detectedArea() {
                return false;
            }
        };

        public boolean detectedArea() {
            return true;
        }
    }

    /**
     * 判断 (x,y) 对应的点是否处于二维码矩阵的探测图形内
     *
     * @param x                目标点的x坐标
     * @param y                目标点的y坐标
     * @param matrixW          二维码矩阵宽
     * @param matrixH          二维码矩阵高
     * @param detectCornerSize 探测图形的大小
     * @return
     */
    private static DetectLocation inDetectCornerArea(int x, int y, int matrixW, int matrixH, int detectCornerSize) {
        if (x < detectCornerSize && y < detectCornerSize) {
            // 左上角
            return DetectLocation.LT;
        }

        if (x < detectCornerSize && y >= matrixH - detectCornerSize) {
            // 左下角
            return DetectLocation.LD;
        }

        if (x >= matrixW - detectCornerSize && y < detectCornerSize) {
            // 右上角
            return DetectLocation.RT;
        }

        return DetectLocation.NONE;
    }

    /**
     * 判断 (x,y) 对应的点是否为二维码举证探测图形中外面的框, 这个方法的调用必须在确认(x,y)对应的点在探测图形内
     *
     * @param x                目标点的x坐标
     * @param y                目标点的y坐标
     * @param matrixW          二维码矩阵宽
     * @param matrixH          二维码矩阵高
     * @param detectCornerSize 探测图形的大小
     * @return
     */
    private static boolean inOuterDetectCornerArea(int x, int y, int matrixW, int matrixH, int detectCornerSize) {
        if (x == 0 || x == detectCornerSize - 1 || x == matrixW - 1 || x == matrixW - detectCornerSize || y == 0 ||
                y == detectCornerSize - 1 || y == matrixH - 1 || y == matrixH - detectCornerSize) {
            // 外层的框
            return true;
        }

        return false;
    }


    /**
     * 绘制探测图形
     *
     * @param qrCodeConfig     绘制参数
     * @param g2               二维码画布
     * @param bitMatrix        二维码矩阵
     * @param matrixW          二维码矩阵宽
     * @param matrixH          二维码矩阵高
     * @param leftPadding      二维码左边留白距离
     * @param topPadding       二维码上边留白距离
     * @param infoSize         二维码矩阵中一个点对应的像素大小
     * @param detectCornerSize 探测图形大小
     * @param x                目标点x坐标
     * @param y                目标点y坐标
     * @param detectOutColor   探测图形外边圈的颜色
     * @param detectInnerColor 探测图形内部圈的颜色
     */
    private static void drawDetectImg(QrCodeOptions qrCodeConfig, Graphics2D g2, BitMatrixEx bitMatrix, int matrixW,
                                      int matrixH, int leftPadding, int topPadding, int infoSize, int detectCornerSize, int x, int y,
                                      Color detectOutColor, Color detectInnerColor, DetectLocation detectLocation) {

        BufferedImage detectedImg = qrCodeConfig.getDetectOptions().chooseDetectedImg(detectLocation);
        if (detectedImg != null) {
            // 使用探测图形的图片来渲染
            g2.drawImage(detectedImg
                            .getScaledInstance(infoSize * detectCornerSize, infoSize * detectCornerSize, Image.SCALE_SMOOTH),
                    leftPadding + x * infoSize, topPadding + y * infoSize, null);

            // 图片直接渲染完毕之后，将其他探测图形的点设置为0，表示不需要再次渲染
            for (int addX = 0; addX < detectCornerSize; addX++) {
                for (int addY = 0; addY < detectCornerSize; addY++) {
                    bitMatrix.getByteMatrix().set(x + addX, y + addY, 0);
                }
            }
            return;
        }

        if (inOuterDetectCornerArea(x, y, matrixW, matrixH, detectCornerSize)) {
            // 外层的框
            g2.setColor(detectOutColor);
        } else {
            // 内层的框
            g2.setColor(detectInnerColor);
        }

        g2.fillRect(leftPadding + x * infoSize, topPadding + y * infoSize, infoSize, infoSize);
    }

    private static void drawQrDotBgImg(QrCodeOptions qrCodeConfig, Graphics2D g2, int leftPadding, int topPadding,
                                       int infoSize, int x, int y) {
        // 如果没有指定二维码中0点对应的背景图，则不做任何处理
        if (qrCodeConfig.getDrawOptions().getBgImg() == null) {
            return;
        }

        // 绘制二维码背景图
        QrCodeOptions.DrawStyle.IMAGE
                .draw(g2, leftPadding + x * infoSize, topPadding + y * infoSize, infoSize, infoSize,
                        qrCodeConfig.getDrawOptions().getBgImg(), null);
    }


    /**
     * 绘制二维码中的像素点图形
     *
     * @param qrCodeConfig 绘制参数
     * @param drawStyle    绘制的图形样式
     * @param g2           二维码画布
     * @param bitMatrix    二维码矩阵
     * @param leftPadding  二维码左边留白距离
     * @param topPadding   二维码上边留白距离
     * @param infoSize     二维码矩阵中一个点对应的像素大小
     * @param x            目标点x坐标
     * @param y            目标点y坐标
     */
    private static void drawQrDotImg(QrCodeOptions qrCodeConfig, QrCodeOptions.DrawStyle drawStyle, Graphics2D g2,
                                     BitMatrixEx bitMatrix, int leftPadding, int topPadding, int infoSize, int x, int y) {

        if (drawStyle != QrCodeOptions.DrawStyle.IMAGE) {
            drawGeometricFigure(qrCodeConfig, drawStyle, g2, bitMatrix, leftPadding, topPadding, infoSize, x, y);
        } else {
            drawSpecialImg(qrCodeConfig, drawStyle, g2, bitMatrix, leftPadding, topPadding, infoSize, x, y);
        }
    }

    /**
     * 绘制自定义的几种几何图形
     *
     * @param qrCodeConfig 绘制参数
     * @param drawStyle    绘制的图形样式
     * @param g2           二维码画布
     * @param bitMatrix    二维码矩阵
     * @param leftPadding  二维码左边留白距离
     * @param topPadding   二维码上边留白距离
     * @param infoSize     二维码矩阵中一个点对应的像素大小
     * @param x            目标点x坐标
     * @param y            目标点y坐标
     */
    private static void drawGeometricFigure(QrCodeOptions qrCodeConfig, QrCodeOptions.DrawStyle drawStyle,
                                            Graphics2D g2, BitMatrixEx bitMatrix, int leftPadding, int topPadding, int infoSize, int x, int y) {
        if (!qrCodeConfig.getDrawOptions().isEnableScale()) {
            // 用几何图形进行填充时，如果不支持多个像素点渲染一个几何图形时，直接返回即可
            drawStyle.draw(g2, leftPadding + x * infoSize, topPadding + y * infoSize, infoSize, infoSize,
                    qrCodeConfig.getDrawOptions().getImage(1, 1), qrCodeConfig.getDrawOptions().getDrawQrTxt());
            return;
        }

        int maxRow = getMaxRow(bitMatrix.getByteMatrix(), x, y);
        int maxCol = getMaxCol(bitMatrix.getByteMatrix(), x, y);
        List<DotSize> availableSize = getAvailableSize(bitMatrix.getByteMatrix(), x, y, maxRow, maxCol);
        for (DotSize dotSize : availableSize) {
            if (!drawStyle.expand(dotSize)) {
                continue;
            }

            // 开始绘制，并将已经绘制过得地方置空
            drawStyle.draw(g2, leftPadding + x * infoSize, topPadding + y * infoSize, dotSize.getCol() * infoSize,
                    dotSize.getRow() * infoSize, qrCodeConfig.getDrawOptions().getImage(dotSize),
                    qrCodeConfig.getDrawOptions().getDrawQrTxt());
            for (int col = 0; col < dotSize.getCol(); col++) {
                for (int row = 0; row < dotSize.getRow(); row++) {
                    bitMatrix.getByteMatrix().set(x + col, y + row, 0);
                }
            }
            return;

        }

        drawStyle.draw(g2, leftPadding + x * infoSize, topPadding + y * infoSize, infoSize, infoSize,
                qrCodeConfig.getDrawOptions().getImage(1, 1), qrCodeConfig.getDrawOptions().getDrawQrTxt());
    }


    /**
     * 绘制指定的图片
     *
     * @param qrCodeConfig 绘制参数
     * @param drawStyle    绘制的图形样式
     * @param g2           二维码画布
     * @param bitMatrix    二维码矩阵
     * @param leftPadding  二维码左边留白距离
     * @param topPadding   二维码上边留白距离
     * @param infoSize     二维码矩阵中一个点对应的像素大小
     * @param x            目标点x坐标
     * @param y            目标点y坐标
     */
    private static void drawSpecialImg(QrCodeOptions qrCodeConfig, QrCodeOptions.DrawStyle drawStyle, Graphics2D g2,
                                       BitMatrixEx bitMatrix, int leftPadding, int topPadding, int infoSize, int x, int y) {
        // 针对图片扩展的方式，支持更加灵活的填充方式
        int maxRow = getMaxRow(bitMatrix.getByteMatrix(), x, y);
        int maxCol = getMaxCol(bitMatrix.getByteMatrix(), x, y);
        List<DotSize> availableSize = getAvailableSize(bitMatrix.getByteMatrix(), x, y, maxRow, maxCol);
        // 获取对应的图片
        BufferedImage drawImg;
        for (DotSize dotSize : availableSize) {
            drawImg = qrCodeConfig.getDrawOptions().getImage(dotSize);
            if (drawImg == null) {
                continue;
            }

            // 开始绘制，并将已经绘制过得地方置空
            drawStyle.draw(g2, leftPadding + x * infoSize, topPadding + y * infoSize, dotSize.getCol() * infoSize,
                    dotSize.getRow() * infoSize, drawImg, qrCodeConfig.getDrawOptions().getDrawQrTxt());
            for (int col = 0; col < dotSize.getCol(); col++) {
                for (int row = 0; row < dotSize.getRow(); row++) {
                    bitMatrix.getByteMatrix().set(x + col, y + row, 0);
                }
            }
            return;
        }

        // 如果上面全部没有满足，则使用兜底的绘制
        drawStyle.draw(g2, leftPadding + x * infoSize, topPadding + y * infoSize, infoSize, infoSize,
                qrCodeConfig.getDrawOptions().getImage(DotSize.SIZE_1_1), qrCodeConfig.getDrawOptions().getDrawQrTxt());
    }

    /**
     * 获取矩阵中从(x,y)出发最大连续为1的行数
     *
     * @param bitMatrix 矩阵
     * @param x         起始点x
     * @param y         起始点y
     * @return
     */
    private static int getMaxRow(ByteMatrix bitMatrix, int x, int y) {
        int cnt = 1;
        while (++y < bitMatrix.getHeight()) {
            if (bitMatrix.get(x, y) == 0) {
                break;
            }
            ++cnt;
        }
        return cnt;
    }

    /**
     * 获取矩阵中从(x,y)出发最大连续为1的列数
     *
     * @param bitMatrix 矩阵
     * @param x         起始点x
     * @param y         起始点y
     * @return
     */
    private static int getMaxCol(ByteMatrix bitMatrix, int x, int y) {
        int cnt = 1;
        while (++x < bitMatrix.getWidth()) {
            if (bitMatrix.get(x, y) == 0) {
                break;
            }
            ++cnt;
        }
        return cnt;
    }

    private static List<DotSize> getAvailableSize(ByteMatrix bitMatrix, int x, int y, int maxRow, int maxCol) {
        if (maxRow == 1) {
            return Collections.singletonList(DotSize.create(1, maxCol));
        }

        if (maxCol == 1) {
            return Collections.singletonList(DotSize.create(maxRow, 1));
        }

        List<DotSize> container = new ArrayList<>();

        int col = 1;
        int lastRow = maxRow;
        while (col < maxCol) {
            int offset = 0;
            int row = 1;
            while (++offset < lastRow) {
                if (bitMatrix.get(x + col, y + offset) == 0) {
                    break;
                }
                ++row;
            }
            ++col;
            lastRow = row;
            container.add(new DotSize(row, col));
        }

        container.sort((o1, o2) -> o2.size() - o1.size());
        return container;
    }
}
