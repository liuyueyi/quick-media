package com.hust.hui.quickmedia.common.util;

import com.hust.hui.quickmedia.common.image.ImgCreateOptions;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by yihui on 2017/8/16.
 */
public class GraphicUtil {

    public static BufferedImage createImg(int w, int h, BufferedImage img) {
        BufferedImage bf = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bf.createGraphics();

        if (img != null) {
            g2d.setComposite(AlphaComposite.Src);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(img, 0, 0, null);
        }
        g2d.dispose();
        return bf;
    }


    public static Graphics2D getG2d(BufferedImage bf) {
        Graphics2D g2d = bf.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        return g2d;
    }


    /**
     * 在原图上绘制图片
     *
     * @param source  原图
     * @param dest    待绘制图片
     * @param y       待绘制的y坐标
     * @param options
     * @return 绘制图片的高度
     */
    public static int drawImage(BufferedImage source,
                                BufferedImage dest,
                                int y,
                                ImgCreateOptions options) {
        Graphics2D g2d = getG2d(source);
        int w = Math.min(dest.getWidth(), options.getImgW() - (options.getLeftPadding() << 1));
        int h = w * dest.getHeight() / dest.getWidth();

        int x = calOffsetX(options.getLeftPadding(),
                options.getImgW(), w, options.getAlignStyle());
        g2d.drawImage(dest,
                x,
                y + options.getLinePadding(),
                w,
                h,
                null);
        g2d.dispose();

        return h;
    }


    public static int drawContent(Graphics2D g2d,
                                  String content,
                                  int y,
                                  ImgCreateOptions options) {

        int w = options.getImgW();
        int leftPadding = options.getLeftPadding();
        int linePadding = options.getLinePadding();
        Font font = options.getFont();


        // 一行容纳的字符个数
        int lineNum = (int) Math.floor((w - (leftPadding << 1)) / (double) font.getSize());

        String[] strs = splitStr(content, lineNum);

        g2d.setFont(font);

        g2d.setColor(options.getFontColor());
        int index = 0;
        int x;
        for (String tmp : strs) {
            x = calOffsetX(leftPadding, w, tmp.length() * font.getSize(), options.getAlignStyle());
            g2d.drawString(tmp, x, y + (linePadding + font.getSize()) * index);
            index++;
        }


        return y + (linePadding + font.getSize()) * (index);
    }

    private static int calOffsetX(int padding,
                                  int width,
                                  int strSize,
                                  ImgCreateOptions.AlignStyle style) {
        if (style == ImgCreateOptions.AlignStyle.LEFT) {
            return padding;
        } else if (style == ImgCreateOptions.AlignStyle.RIGHT) {
            return width - padding - strSize;
        } else {
            return (width - strSize) >> 1;
        }
    }


    /**
     * 按照长度对字符串进行分割
     * <p>
     * fixme 包含emoj表情时，兼容一把
     *
     * @param str      原始字符串
     * @param splitLen 分割的长度
     * @return
     */
    public static String[] splitStr(String str, int splitLen) {
        int len = str.length();
        int size = (int) Math.ceil(len / (float) splitLen);

        String[] ans = new String[size];
        int start = 0;
        int end = splitLen;
        for (int i = 0; i < size; i++) {
            ans[i] = str.substring(start, end > len ? len : end);
            start = end;

            end += splitLen;
        }

        return ans;
    }
}
