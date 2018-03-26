package com.github.hui.quick.plugin.qrcode.util;

import com.github.hui.quick.plugin.base.ColorUtil;
import com.github.hui.quick.plugin.base.ImageOperateUtil;
import com.github.hui.quick.plugin.qrcode.wrapper.BitMatrixEx;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by yihui on 2017/4/7.
 */
@Slf4j
public class QrCodeUtil {


    /**
     * 绘制logo图片
     *
     * @param source
     * @param logoOptions
     * @return
     */
    public static BufferedImage drawLogo(BufferedImage source, QrCodeOptions.LogoOptions logoOptions) {
        int QRCODE_WIDTH = source.getWidth();
        int QRCODE_HEIGHT = source.getHeight();

        // 获取logo图片
        BufferedImage bf = logoOptions.getLogo();

        // 绘制圆角图片
        int radius = 0;
        if (logoOptions.getLogoStyle() == QrCodeOptions.LogoStyle.ROUND) {
            radius = bf.getWidth() >> 2;
            bf = ImageOperateUtil.makeRoundedCorner(bf, radius);
        }


        // 绘制边框
        if (logoOptions.isBorder()) {
            bf = ImageOperateUtil.makeRoundBorder(bf, radius, logoOptions.getBorderColor());
        }


        // logo的宽高
        int logoRate = logoOptions.getRate();
        int w = bf.getWidth() > (QRCODE_WIDTH << 1) / logoRate ? (QRCODE_WIDTH << 1) / logoRate : bf.getWidth();
        int h = bf.getHeight() > (QRCODE_HEIGHT << 1) / logoRate ? (QRCODE_HEIGHT << 1) / logoRate : bf.getHeight();

        int x = (QRCODE_WIDTH - w) >> 1;
        int y = (QRCODE_HEIGHT - h) >> 1;


        // 插入LOGO
        Graphics2D g2 = source.createGraphics();

        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(bf, x, y, w, h, null);
        g2.dispose();
        bf.flush();
        return source;
    }




    /**
     * 绘制背景图
     *
     * @param source       二维码图
     * @param bgImgOptions 背景图信息
     * @return
     */
    public static BufferedImage drawBackground(BufferedImage source, QrCodeOptions.BgImgOptions bgImgOptions) {
        int sW = source.getWidth();
        int sH = source.getHeight();

        // 背景的图宽高不应该小于原图
        int bgW = bgImgOptions.getBgW() < sW ? sW : bgImgOptions.getBgW();
        int bgH = bgImgOptions.getBgH() < sH ? sH : bgImgOptions.getBgH();


        // 背景图缩放
        BufferedImage bg = bgImgOptions.getBgImg();
        if (bg.getWidth() != bgW || bg.getHeight() != bgH) {
            BufferedImage temp = new BufferedImage(bgW, bgH, BufferedImage.TYPE_INT_ARGB);
            temp.getGraphics().drawImage(bg.getScaledInstance(bgW, bgH, Image.SCALE_SMOOTH)
                    , 0, 0, null);
            bg = temp;
        }

        Graphics2D g2d = bg.createGraphics();
        if (bgImgOptions.getBgImgStyle() == QrCodeOptions.BgImgStyle.FILL) {
            // 选择一块区域进行填充
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(source, bgImgOptions.getStartX(), bgImgOptions.getStartY(), sW, sH, null);
        } else {
            // 覆盖方式
            int x = (bgW - sW) >> 1;
            int y = (bgH - sH) >> 1;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, bgImgOptions.getOpacity())); // 透明度， 避免看不到背景
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(source, x, y, sW, sH, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
        }
        g2d.dispose();
        bg.flush();
        return bg;
    }


    public static BufferedImage drawQrInfo(QrCodeOptions qrCodeConfig, BitMatrixEx bitMatrix) {
        int qrCodeWidth = bitMatrix.getWidth();
        int qrCodeHeight = bitMatrix.getHeight();
        int infoSize = bitMatrix.getMultiple();
        BufferedImage qrCode = new BufferedImage(qrCodeWidth, qrCodeHeight, BufferedImage.TYPE_INT_ARGB);


        // 绘制的背景色
        Color bgColor = qrCodeConfig.getDrawOptions().getBgColor();
        // 绘制前置色
        Color preColor = qrCodeConfig.getDrawOptions().getPreColor();

        if (qrCodeConfig.getBgImgOptions() != null && qrCodeConfig.getBgImgOptions().getBgImgStyle() == QrCodeOptions.BgImgStyle.PENETRATE) {
            // 透传，用背景图颜色进行绘制时
            preColor = ColorUtil.OPACITY;
        }

        // 探测图形外圈的颜色
        Color detectOutColor = qrCodeConfig.getDetectOptions().getOutColor();
        // 探测图形内圈的颜色
        Color detectInnerColor = qrCodeConfig.getDetectOptions().getInColor();


        int leftPadding = bitMatrix.getLeftPadding();
        int topPadding = bitMatrix.getTopPadding();

        Graphics2D g2 = qrCode.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        // 直接背景铺满整个图
        g2.setColor(bgColor);
        g2.fillRect(0, 0, qrCodeWidth, qrCodeHeight);

        // 探测图形的大小
        int detectCornerSize = bitMatrix.getByteMatrix().get(0, 5) == 1 ? 7 : 5;

        int byteW = bitMatrix.getByteMatrix().getWidth();
        int byteH = bitMatrix.getByteMatrix().getHeight();

        boolean row2 = false;
        boolean col2 = false;
        QrCodeOptions.DrawStyle drawStyle = qrCodeConfig.getDrawOptions().getDrawStyle();
        for (int x = 0; x < byteW; x++) {
            for (int y = 0; y < byteH; y++) {
                if (bitMatrix.getByteMatrix().get(x, y) == 0) {
                    continue;
                }

                // 设置三个位置探测图形
                if (x < detectCornerSize && y < detectCornerSize // 左上角
                        || (x < detectCornerSize && y >= byteH - detectCornerSize) // 左下脚
                        || (x >= byteW - detectCornerSize && y < detectCornerSize)) { // 右上角

                    if (qrCodeConfig.getDetectOptions().getDetectImg() != null) {
                        g2.drawImage(qrCodeConfig.getDetectOptions().getDetectImg(),
                                leftPadding + x * infoSize, topPadding + y * infoSize,
                                infoSize * detectCornerSize, infoSize * detectCornerSize, null);

                        for (int addX = 0; addX < detectCornerSize; addX++) {
                            for (int addY = 0; addY < detectCornerSize; addY++) {
                                bitMatrix.getByteMatrix().set(x + addX, y + addY, 0);
                            }
                        }
                        continue;
                    }


                    if (x == 0 || x == detectCornerSize - 1 || x == byteW - 1 || x == byteW - detectCornerSize
                            || y == 0 || y == detectCornerSize - 1 || y == byteH - 1 || y == byteH - detectCornerSize) {
                        // 外层的框
                        g2.setColor(detectOutColor);
                    } else {
                        // 内层的框
                        g2.setColor(detectInnerColor);
                    }

                    g2.fillRect(leftPadding + x * infoSize, topPadding + y * infoSize, infoSize, infoSize);
                } else { // 着色二维码主题
                    g2.setColor(preColor);

                    if (!qrCodeConfig.getDrawOptions().isEnableScale()) {
                        drawStyle.draw(g2,
                                leftPadding + x * infoSize,
                                topPadding + y * infoSize,
                                infoSize,
                                infoSize,
                                qrCodeConfig.getDrawOptions().getImg());
                        continue;
                    }


                    // 支持拓展时
                    row2 = rightTrue(bitMatrix.getByteMatrix(), x, y);
                    col2 = belowTrue(bitMatrix.getByteMatrix(), x, y);

                    if (row2 && col2 && diagonalTrue(bitMatrix.getByteMatrix(), x, y) &&
                            qrCodeConfig.getDrawOptions().enableScale(QrCodeOptions.ExpandType.SIZE4)) {
                        // 四个相等
                        bitMatrix.getByteMatrix().set(x + 1, y, 0);
                        bitMatrix.getByteMatrix().set(x + 1, y + 1, 0);
                        bitMatrix.getByteMatrix().set(x, y + 1, 0);
                        drawStyle.draw(g2,
                                leftPadding + x * infoSize,
                                topPadding + y * infoSize,
                                infoSize << 1,
                                infoSize << 1,
                                qrCodeConfig.getDrawOptions().getSize4Img());
                    } else if (row2 && qrCodeConfig.getDrawOptions().enableScale(QrCodeOptions.ExpandType.ROW2)) { // 横向相同
                        bitMatrix.getByteMatrix().set(x + 1, y, 0);
                        drawStyle.draw(g2,
                                leftPadding + x * infoSize,
                                topPadding + y * infoSize,
                                infoSize << 1,
                                infoSize,
                                qrCodeConfig.getDrawOptions().getRow2Img());
                    } else if (col2 && qrCodeConfig.getDrawOptions().enableScale(QrCodeOptions.ExpandType.COL2)) { // 列的两个
                        bitMatrix.getByteMatrix().set(x, y + 1, 0);
                        drawStyle.draw(g2,
                                leftPadding + x * infoSize,
                                topPadding + y * infoSize,
                                infoSize,
                                infoSize << 1,
                                qrCodeConfig.getDrawOptions().getCol2img());
                    } else {
                        drawStyle.draw(g2,
                                leftPadding + x * infoSize,
                                topPadding + y * infoSize,
                                infoSize,
                                infoSize,
                                qrCodeConfig.getDrawOptions().getImg());
                    }
                }
            }
        }
        g2.dispose();
        return qrCode;
    }


    private static boolean rightTrue(ByteMatrix byteMatrix, int x, int y) {
        return x + 1 < byteMatrix.getWidth() && byteMatrix.get(x + 1, y) == 1;
    }

    private static boolean belowTrue(ByteMatrix byteMatrix, int x, int y) {
        return y + 1 < byteMatrix.getHeight() && byteMatrix.get(x, y + 1) == 1;
    }

    // 对角是否相等
    private static boolean diagonalTrue(ByteMatrix byteMatrix, int x, int y) {
        return byteMatrix.get(x + 1, y + 1) == 1;
    }
}
