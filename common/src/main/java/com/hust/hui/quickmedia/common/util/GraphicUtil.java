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
        if (bf == null) {
            bf = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        }
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

        g2d.setFont(options.getFont());

        int lineLen = w - (leftPadding << 1);
        String[] strs = splitStr(content, lineLen, g2d.getFontMetrics());


        g2d.setColor(options.getFontColor());
        int index = 0;
        int x;
        for (String tmp : strs) {
            x = calOffsetX(leftPadding, w, g2d.getFontMetrics().stringWidth(tmp), options.getAlignStyle());
            g2d.drawString(tmp, x, y + (linePadding + g2d.getFontMetrics().getHeight()) * index);
            index++;
        }


        return y + (linePadding + g2d.getFontMetrics().getHeight()) * (index);
    }


    /**
     * 计算不同对其方式时，对应的x坐标
     *
     * @param padding 左右边距
     * @param width   图片总宽
     * @param strSize 字符串总长
     * @param style   对其方式
     * @return 返回计算后的x坐标
     */
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
     * 将字符串根据每行容纳的长度进行分割为多行
     *
     * @param str         待分割的字符串内容
     * @param lineLen     一行长度
     * @param fontMetrics 字体信息
     * @return
     */
    public static String[] splitStr(String str, int lineLen, FontMetrics fontMetrics) {
        int lineNum = (int) Math.ceil(fontMetrics.stringWidth(str) / (float) lineLen);

        if (lineNum == 1) {
            return new String[]{str};
        }


        String[] ans = new String[lineNum];
        int strLen = str.length();
        int lastTotal = 0;
        int lastIndex = 0;
        int ansIndex = 0;
        int tmpLen;
        for (int i = 0; i < strLen; i++) {
            tmpLen = fontMetrics.charWidth(str.charAt(i));
            lastTotal += tmpLen;
            if (lastTotal > lineLen) {
                ans[ansIndex++] = str.substring(lastIndex, i);
                lastIndex = i;
                lastTotal = tmpLen;
            }
        }

        if (lastIndex < strLen) {
            ans[ansIndex] = str.substring(lastIndex);
        }

        return ans;
    }


    /**
     * 计算总行数
     *
     * @param strs        字符串列表
     * @param lineLen     每行容纳内容的长度
     * @param fontMetrics 字体信息
     * @return
     */
    public static int calLineNum(String[] strs, int lineLen, FontMetrics fontMetrics) {
        int totalLine = 0;
        for (String str : strs) {
            totalLine += Math.ceil(fontMetrics.stringWidth(str) / (float) lineLen);
        }

        return totalLine;
    }
}
