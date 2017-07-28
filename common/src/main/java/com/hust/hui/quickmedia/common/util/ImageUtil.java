package com.hust.hui.quickmedia.common.util;

import com.hust.hui.quickmedia.common.qrcode.QrCodeOptions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by yihui on 2017/4/7.
 */
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
        bf = ImageUtil.makeRoundBorder(bf, logoStyle, size, logoBgColor); // 边距为二维码图片的1/15

        // logo的宽高
        int w = bf.getWidth() > QRCODE_WIDTH * 2 / 10 ? QRCODE_WIDTH * 2 / 10 : bf.getWidth();
        int h = bf.getHeight() > QRCODE_HEIGHT * 2 / 10 ? QRCODE_HEIGHT * 2 / 10 : bf.getHeight();

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
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.8f)); // 透明度， 避免看不到背景
        g2d.drawImage(source, x, y, sW, sH, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
        g2d.dispose();
        bg.flush();
        return bg;
    }
}
