package com.hust.hui.quickmedia.common.util;

import com.hust.hui.quickmedia.common.qrcode.BitMatrixEx;
import com.hust.hui.quickmedia.common.qrcode.QrCodeOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by yihui on 2017/4/7.
 */
@Slf4j
public class ImageUtil {

    /**
     * 在图片中间,插入圆角的logo
     *
     * @param qrCode      原图
     * @param logo        logo地址
     * @param logoStyle   logo 的样式 （圆角， 直角）
     * @param logoBgColor logo的背景色
     * @throws IOException
     */
    public static void insertLogo(BufferedImage qrCode,
                                  String logo,
                                  QrCodeOptions.LogoStyle logoStyle,
                                  Color logoBgColor) throws IOException {
        int QRCODE_WIDTH = qrCode.getWidth();
        int QRCODE_HEIGHT = qrCode.getHeight();

        // 获取logo图片
        BufferedImage bf = getImageByPath(logo);
        int size = bf.getWidth() / 15;
        bf = ImageUtil.makeRoundBorder(bf, logoStyle, size, logoBgColor); // 边距为logo的1/15

        // logo的宽高
        int logoRate = 12;
        int w = bf.getWidth() > QRCODE_WIDTH * 2 / logoRate ? QRCODE_WIDTH * 2 / logoRate : bf.getWidth();
        int h = bf.getHeight() > QRCODE_HEIGHT * 2 / logoRate ? QRCODE_HEIGHT * 2 / logoRate : bf.getHeight();

        // 插入LOGO
        Graphics2D graph = qrCode.createGraphics();

        int x = (QRCODE_WIDTH - w) >> 1;
        int y = (QRCODE_HEIGHT - h) >> 1;

        graph.drawImage(bf, x, y, w, h, null);
        graph.dispose();
        bf.flush();
    }


    /**
     * 根据路径获取图片
     *
     * @param path 本地路径 or 网络地址
     * @return 图片
     * @throws IOException
     */
    public static BufferedImage getImageByPath(String path) throws IOException {
        if (StringUtils.isBlank(path)) {
            return null;
        }

        if (path.startsWith("http")) { // 从网络获取logo
//            return ImageIO.read(new URL(path));
            return ImageIO.read(HttpUtil.downFile(path));
        } else if (path.startsWith("/")) { // 绝对地址获取logo
            return ImageIO.read(new File(path));
        } else { // 从资源目录下获取logo
            return ImageIO.read(ImageUtil.class.getClassLoader().getResourceAsStream(path));
        }
    }


    /**
     * <p>
     * 生成圆角图片 & 圆角边框
     *
     * @param image     原图
     * @param logoStyle 圆角的角度
     * @param size      边框的边距
     * @param color     边框的颜色
     * @return 返回带边框的圆角图
     */
    public static BufferedImage makeRoundBorder(BufferedImage image,
                                                QrCodeOptions.LogoStyle logoStyle,
                                                int size, Color color) {
        // 将图片变成圆角
        int cornerRadius = 0;
        if (logoStyle == QrCodeOptions.LogoStyle.ROUND) {
            cornerRadius = image.getWidth() / 4;
            image = makeRoundedCorner(image, cornerRadius);
        }

        int w = image.getWidth() + size;
        int h = image.getHeight() + size;
        BufferedImage output = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color == null ? Color.WHITE : color);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius,
                cornerRadius));

        // ... then compositing the image on top,
        // using the white shape from above as alpha source
//        g2.setComposite(AlphaComposite.SrcAtop);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
        g2.drawImage(image, size / 2, size / 2, null);
        g2.dispose();

        return output;
    }


    /**
     * 生成圆角图片
     *
     * @param image        原始图片
     * @param cornerRadius 圆角的弧度
     * @return 返回圆角图
     */
    public static BufferedImage makeRoundedCorner(BufferedImage image,
                                                  int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();

        // This is what we want, but it only does hard-clipping, i.e. aliasing
        // g2.setClip(new RoundRectangle2D ...)

        // so instead fake soft-clipping by first drawing the desired clip shape
        // in fully opaque white with antialiasing enabled...
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius,
                cornerRadius));

        // ... then compositing the image on top,
        // using the white shape from above as alpha source
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        return output;
    }


    /**
     * 绘制背景图
     *
     * @param source     原图
     * @param background 背景图
     * @param bgW        背景图宽
     * @param bgH        背景图高
     * @return
     * @throws IOException
     */
    public static BufferedImage drawBackground(BufferedImage source, String background, int bgW, int bgH) throws IOException {
        int sW = source.getWidth();
        int sH = source.getHeight();


        // 背景的图宽高不应该小于原图
        if (bgW < sW) {
            bgW = sW;
        }

        if (bgH < sH) {
            bgH = sH;
        }


        // 获取背景图
        BufferedImage bg = getImageByPath(background);
        if (bg.getWidth() != bgW || bg.getHeight() != bgH) { // 需要缩放
            BufferedImage temp = new BufferedImage(bgW, bgH, BufferedImage.TYPE_INT_ARGB);
            temp.getGraphics().drawImage(bg.getScaledInstance(bgW, bgH, Image.SCALE_SMOOTH)
                    , 0, 0, null);
            bg = temp;
        }


        // 绘制背景图
        int x = (bgW - sW) >> 1;
        int y = (bgH - sH) >> 1;
        Graphics2D g2d = bg.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.85f)); // 透明度， 避免看不到背景
        g2d.drawImage(source, x, y, sW, sH, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
        g2d.dispose();
        bg.flush();
        return bg;
    }


    public static BufferedImage drawQrInfoV2(QrCodeOptions qrCodeConfig, BitMatrixEx bitMatrix) {
        int qrCodeWidth = bitMatrix.getWidth();
        int qrCodeHeight = bitMatrix.getHeight();
        int infoSize = bitMatrix.getMultiple();
        BufferedImage qrCode = new BufferedImage(qrCodeWidth, qrCodeHeight, BufferedImage.TYPE_INT_RGB);


        Color bgColor = ColorUtil.int2color(qrCodeConfig.getMatrixToImageConfig().getPixelOffColor());
        Color preColor = ColorUtil.int2color(qrCodeConfig.getMatrixToImageConfig().getPixelOnColor());

        Color detectOutColor = ColorUtil.int2color(qrCodeConfig.getDetectCornerColor().getPixelOnColor());
        Color detectInnerColor = ColorUtil.int2color(qrCodeConfig.getDetectCornerColor().getPixelOffColor());

        int leftPadding = bitMatrix.getLeftPadding();
        int topPadding = bitMatrix.getTopPadding();

        Graphics2D g2 = qrCode.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        // 直接背景铺满整个图
        g2.setColor(bgColor);
        g2.fillRect(0, 0, qrCodeWidth, qrCodeHeight);
        BufferedImage drawImg = checkDrawImg(qrCodeConfig);


        int detectCornerSize = bitMatrix.getByteMatrix().get(0, 5) == 1 ? 7 : 5;

        int byteW = bitMatrix.getByteMatrix().getWidth();
        int byteH = bitMatrix.getByteMatrix().getHeight();
        for (int x = 0; x < byteW; x++) {
            for (int y = 0; y < byteH; y++) {
                // 设置三个位置探测图形
                if (x < detectCornerSize && y < detectCornerSize // 左上角
                        || (x < detectCornerSize && y >= byteH - detectCornerSize) // 左下脚
                        || (x >= byteW - detectCornerSize && y < detectCornerSize)) { // 右上角
                    if (bitMatrix.getByteMatrix().get(x, y) == 0) {
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
                } else if (bitMatrix.getByteMatrix().get(x, y) == 1) { // 着色二维码主题
                    g2.setColor(preColor);

                    if (x + 1 < byteW && y + 1 < byteH &&
                            bitMatrix.getByteMatrix().get(x + 1, y) == 1 &&
                            bitMatrix.getByteMatrix().get(x, y + 1) == 1 &&
                            bitMatrix.getByteMatrix().get(x + 1, y + 1) == 1) {
                        bitMatrix.getByteMatrix().set(x + 1, y, 0);
                        bitMatrix.getByteMatrix().set(x + 1, y + 1, 0);
                        bitMatrix.getByteMatrix().set(x, y + 1, 0);
                        qrCodeConfig.getDrawStyle().draw(g2, leftPadding + x * infoSize, topPadding + y * infoSize, infoSize << 1, drawImg);
                    } else {
                        qrCodeConfig.getDrawStyle().draw(g2, leftPadding + x * infoSize, topPadding + y * infoSize, infoSize, drawImg);
                    }
                }
            }
        }
        g2.dispose();
        return qrCode;
    }


    /**
     * 绘制二维码信息
     *
     * @return
     */
    public static BufferedImage drawQrInfo(QrCodeOptions qrCodeConfig, BitMatrixEx bitMatrix) {
        int qrCodeWidth = bitMatrix.getWidth();
        int qrCodeHeight = bitMatrix.getHeight();
        int infoSize = bitMatrix.getMultiple();
        BufferedImage qrCode = new BufferedImage(qrCodeWidth, qrCodeHeight, BufferedImage.TYPE_INT_RGB);


        Color bgColor = ColorUtil.int2color(qrCodeConfig.getMatrixToImageConfig().getPixelOffColor());
        Color preColor = ColorUtil.int2color(qrCodeConfig.getMatrixToImageConfig().getPixelOnColor());

        Color detectOutColor = ColorUtil.int2color(qrCodeConfig.getDetectCornerColor().getPixelOnColor());
        Color detectInnerColor = ColorUtil.int2color(qrCodeConfig.getDetectCornerColor().getPixelOffColor());


        Graphics2D g2 = qrCode.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        // 直接背景铺满整个图
        g2.setColor(bgColor);
        g2.fillRect(0, 0, qrCodeWidth, qrCodeHeight);


        BufferedImage drawImg = checkDrawImg(qrCodeConfig);
        for (int x = bitMatrix.getLeftPadding(); x < qrCodeWidth; x += bitMatrix.getMultiple()) {
            for (int y = bitMatrix.getTopPadding(); y < qrCodeHeight; y += bitMatrix.getMultiple()) {
                // fixme 支持内框二维码的着色
                if (bitMatrix.isDetectCorner(x, y)) { // 着色位置探测图形
                    g2.setColor(bitMatrix.get(x, y) ? detectOutColor : bgColor);
                    g2.fill(new Rectangle2D.Float(x, y, bitMatrix.getMultiple(), bitMatrix.getMultiple()));
                } else if (bitMatrix.get(x, y)) { // 着色二维码主题
                    g2.setColor(preColor);
                    qrCodeConfig.getDrawStyle().draw(g2, x, y, infoSize, drawImg);
                }
            }
        }
        g2.dispose();
        return qrCode;
    }


    private static BufferedImage checkDrawImg(QrCodeOptions qrCodeConfig) {
        BufferedImage drawImg = null;
        try {
            drawImg = ImageUtil.getImageByPath(qrCodeConfig.getDrawImg());
        } catch (Exception e) {
            log.error("load draw img error! e: {}", e);
        }

        if (qrCodeConfig.getDrawStyle() == QrCodeOptions.DrawStyle.IMAGE && drawImg == null) {
            throw new IllegalArgumentException("选择二维码信息绘制图片不存在!");
        }

        return drawImg;
    }
}
