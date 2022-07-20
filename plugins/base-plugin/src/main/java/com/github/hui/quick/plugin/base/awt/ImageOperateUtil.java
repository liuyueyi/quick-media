package com.github.hui.quick.plugin.base.awt;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created by yihui on 2018/3/23.
 */
public class ImageOperateUtil {

    /**
     * 将矩形图变成圆图，常用于logo的处理
     *
     * @param image        原始图片
     * @return 返回圆图
     */
    public static BufferedImage makeRoundImg(BufferedImage image,
                                             boolean borderEnable,
                                             Color borderColor) {
        int size, x, y;
        if(image.getWidth() > image.getHeight()) {
            size = image.getHeight();
            y = 0;
            x = (image.getWidth() - image.getHeight()) >> 1;
        } else {
            size = image.getWidth();
            x = 0;
            y = (image.getHeight()- image.getWidth()) >> 1;
        }

        // 设置原图
        BufferedImage ans =  makeRoundImg(image, new Rectangle(x, y, size, size), size);

        // 设置边框
        if(borderEnable) {
            ans = makeRoundBorder(ans, size, borderColor);
        }
        return ans;
    }


    /**
     * 生成圆角图片
     *
     * @param image
     * @param cornerRadius 圆角的弧度（根据实测效果，一般建议为图片宽度的1/4）, 0表示直角
     * @return
     */
    public static BufferedImage makeRoundImg(BufferedImage image,
                                             Rectangle rectangle,
                                             int cornerRadius) {
        int x = (int) rectangle.getX();
        int y = (int) rectangle.getY();
        int w = (int) rectangle.getWidth();
        int h = (int) rectangle.getHeight();

        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = GraphicUtil.getG2d(output);
        g2.setComposite(AlphaComposite.Src);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));


        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, -x, -y, null);

        g2.dispose();
        return output;
    }



    /**
     * 生成边框
     *
     * @param image        原图
     * @param cornerRadius 角度（根据实测效果，一般建议为图片宽度的1/4）, 0表示直角
     * @param color        边框颜色
     * @return
     */
    public static BufferedImage makeRoundBorder(BufferedImage image,
                                                int cornerRadius,
                                                Color color) {
        int size = image.getWidth() / 15;
        int w = image.getWidth() + size;
        int h = image.getHeight() + size;
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = GraphicUtil.getG2d(output);
        g2.setComposite(AlphaComposite.Src);
        g2.setColor(color == null ? Color.WHITE : color);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
        g2.drawImage(image, size >> 1, size >> 1, null);
        g2.dispose();

        return output;
    }


    /**
     * 生成圆角图片
     *
     * @param image        原始图片
     * @param cornerRadius 圆角的弧度（根据实测效果，一般建议为图片宽度的1/4）, 0表示直角
     * @return 返回圆角图
     */
    public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = GraphicUtil.getG2d(output);
        g2.setComposite(AlphaComposite.Src);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        return output;
    }

}
