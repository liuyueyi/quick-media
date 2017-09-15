package com.hust.hui.quickmedia.common.util;

import com.hust.hui.quickmedia.common.image.ImgCreateOptions;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by yihui on 2017/8/16.
 */
public class GraphicUtil {

    public static BufferedImage createImg(int w, int h, BufferedImage img) {
        return createImg(w, h, 0, 0, img);
    }


    public static BufferedImage createImg(int w, int h, int offsetX, int offsetY, BufferedImage img) {
        BufferedImage bf = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bf.createGraphics();

        if (img != null) {
            g2d.setComposite(AlphaComposite.Src);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(img, offsetX, offsetY, null);
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
        int w = Math.min(dest.getWidth(), options.getImgW() - options.getLeftPadding() - options.getRightPadding());
        int h = w * dest.getHeight() / dest.getWidth();

        int x = calOffsetX(options.getLeftPadding(),
                options.getRightPadding(),
                options.getImgW(),
                w,
                options.getAlignStyle());

        g2d.drawImage(dest,
                x,
                y + options.getLinePadding(),
                w,
                h,
                null);
        g2d.dispose();

        return h;
    }


    public static int drawVerticalImage(BufferedImage source,
                                        BufferedImage dest,
                                        int x,
                                        ImgCreateOptions options) {
        Graphics2D g2d = getG2d(source);
        int h = Math.min(dest.getHeight(), options.getImgH() - options.getTopPadding() - options.getBottomPadding());
        int w = h * dest.getWidth() / dest.getHeight();

        int y = calOffsetY(options.getTopPadding(),
                options.getBottomPadding(),
                options.getImgH(),
                h,
                options.getAlignStyle());


        // xxx 传入的x坐标，即 contentW 实际上已经包含了行间隔，因此不需额外添加
        int drawX = x;
        if (options.getDrawStyle() == ImgCreateOptions.DrawStyle.VERTICAL_RIGHT) {
            drawX = source.getWidth() - w - drawX;
        }
        g2d.drawImage(dest, drawX, y, w, h, null);
        g2d.dispose();
        return w;
    }


    /**
     * 水平内容绘制
     *
     * @param g2d
     * @param content
     * @param y
     * @param options
     * @return
     */
    public static int drawContent(Graphics2D g2d,
                                  String content,
                                  int y,
                                  ImgCreateOptions options) {

        int w = options.getImgW();
        int leftPadding = options.getLeftPadding();
        int rightPadding = options.getRightPadding();
        int linePadding = options.getLinePadding();

        g2d.setFont(options.getFont());

        // 实际填充内容的单行长度
        int lineLen = w - leftPadding - rightPadding;
        String[] strs = splitStr(content, lineLen, g2d.getFontMetrics());


        g2d.setColor(options.getFontColor());
        int index = 0;
        int x;
        for (String tmp : strs) {
            x = calOffsetX(leftPadding, rightPadding, w, g2d.getFontMetrics().stringWidth(tmp), options.getAlignStyle());
            g2d.drawString(tmp, x, y + (linePadding + g2d.getFontMetrics().getHeight()) * index);
            index++;
        }


        return y + (linePadding + g2d.getFontMetrics().getHeight()) * (index);
    }


    /**
     * 垂直文字绘制
     *
     * @param g2d
     * @param content 待绘制的内容
     * @param x       绘制的起始x坐标
     * @param options 配置项
     */
    public static int drawVerticalContent(Graphics2D g2d,
                                           String content,
                                           int x,
                                           ImgCreateOptions options) {
        int topPadding = options.getTopPadding();
        int bottomPadding = options.getBottomPadding();

        g2d.setFont(options.getFont());
        FontMetrics fontMetrics = g2d.getFontMetrics();

        // 实际填充内容的高度， 需要排除上下间距
        int contentH = options.getImgH() - options.getTopPadding() - options.getBottomPadding();
        String[] strs = splitVerticalStr(content, contentH, g2d.getFontMetrics());


        int fontSize = options.getFont().getSize();
        int fontWidth = fontSize + options.getLinePadding();
        if (options.getDrawStyle() == ImgCreateOptions.DrawStyle.VERTICAL_RIGHT) { // 从右往左绘制时，偏移量为负
            fontWidth = -fontWidth;
        }


        g2d.setColor(options.getFontColor());

        int lastX = x, lastY, startY, tmpCharOffsetX=0;
        for (String tmp : strs) {
            lastY = 0;
            startY = calOffsetY(topPadding, bottomPadding, options.getImgH(),
                    fontMetrics.stringWidth(tmp) + fontMetrics.getDescent() * (tmp.length() - 1), options.getAlignStyle())
                    + fontMetrics.getAscent();

            for (int i = 0; i < tmp.length(); i++) {
                if (PunctuationUtil.isPunctuation(tmp.charAt(i))) {
                    tmpCharOffsetX = fontSize >> 1;
                } else {
                    tmpCharOffsetX = 0;
                }

                g2d.drawString(tmp.charAt(i) + "",
                        lastX + tmpCharOffsetX,
                        startY + lastY);

                lastY += g2d.getFontMetrics().charWidth(tmp.charAt(i)) + g2d.getFontMetrics().getDescent();
            }
            lastX += fontWidth;
        }

        return lastX + tmpCharOffsetX;
    }


    /**
     * 计算不同对其方式时，对应的x坐标
     *
     * @param leftPadding  左边距
     * @param rightPadding 右边距
     * @param width        图片总宽
     * @param strSize      字符串总长
     * @param style        对其方式
     * @return 返回计算后的x坐标
     */
    public static int calOffsetX(int leftPadding,
                                  int rightPadding,
                                  int width,
                                  int strSize,
                                  ImgCreateOptions.AlignStyle style) {
        if (style == ImgCreateOptions.AlignStyle.LEFT) {
            return leftPadding;
        } else if (style == ImgCreateOptions.AlignStyle.RIGHT) {
            return width - rightPadding - strSize;
        } else {
            return (width - strSize) >> 1;
        }
    }


    /**
     * 垂直绘制时，根据不同的对其方式，计算起始的y坐标
     * 1. 上对齐，则 y = 上边距
     * 2. 下对其， 则 y = 总高度 - 内容高度 - 下边距
     * 3. 居中， 则 y = (总高度 - 内容高度) / 2
     *
     * @param topPadding    上边距
     * @param bottomPadding 下边距
     * @param height        总高度
     * @param strSize       文本内容对应绘制的高度
     * @param style         对其样式
     * @return
     */
    public static int calOffsetY(int topPadding,
                                  int bottomPadding,
                                  int height,
                                  int strSize,
                                  ImgCreateOptions.AlignStyle style) {
        if (style == ImgCreateOptions.AlignStyle.TOP) {
            return topPadding;
        } else if (style == ImgCreateOptions.AlignStyle.BOTTOM) {
            return height - bottomPadding - strSize;
        } else {
            return (height - strSize) >> 1;
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
        int lineNum = (int) Math.ceil((fontMetrics.stringWidth(str)) / (float) lineLen);

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


    public static String[] splitVerticalStr(String str, int lineLen, FontMetrics fontMetrics) {
        int l = fontMetrics.getDescent() * (str.length() - 1);
        int lineNum = (int) Math.ceil((fontMetrics.stringWidth(str) + l) / (float) lineLen);

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
            tmpLen = fontMetrics.charWidth(str.charAt(i)) + fontMetrics.getDescent();
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


    public static int calVerticalLineNum(String[] strs, int lineLen, FontMetrics fontMetrics) {
        int totalLine = 0;
        for (String str : strs) {
            totalLine += Math.ceil((fontMetrics.stringWidth(str) + (str.length() - 1) * fontMetrics.getDescent()) / (float) lineLen);
        }
        return totalLine;
    }
}
