package com.github.hui.quick.plugin.image.helper;

import com.github.hui.quick.plugin.base.GraphicUtil;
import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;
import com.github.hui.quick.plugin.image.util.PunctuationUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Created by yihui on 2018/3/26.
 */
public class ImgDrawHelper {

    /**
     * 水平内容绘制
     *
     * @param g2d
     * @param content
     * @param endIndex
     * @param y
     * @param options
     * @return
     */
    public static int drawContent(Graphics2D g2d,
                                  String content,
                                  int endIndex,
                                  int y,
                                  ImgCreateOptions options) {

        int w = options.getImgW();
        int leftPadding = options.getLeftPadding();
        int rightPadding = options.getRightPadding();
        int linePadding = options.getLinePadding();

        g2d.setFont(options.getFont());

        // 实际填充内容的单行长度
        int lineLen = w - leftPadding - rightPadding;
        String[] strs = CalculateHelper.splitStr(content, lineLen, g2d.getFontMetrics());


        g2d.setColor(options.getFontColor());
        int index = 0;
        int x, count = 0;
        int tempEndIndex;
        for (String tmp : strs) {
            x = CalculateHelper.calOffsetX(leftPadding, rightPadding, w, g2d.getFontMetrics().stringWidth(tmp), options.getAlignStyle());

            if (count + tmp.length() < endIndex) {
                tempEndIndex = tmp.length();
            } else {
                tempEndIndex = endIndex - count;
            }

            g2d.drawString(tmp.substring(0, tempEndIndex), x, y + (linePadding + g2d.getFontMetrics().getHeight()) * index);

            index++;

            count += tmp.length();
            if (count > endIndex) {
                break;
            }
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
                                          int endIndex,
                                          int x,
                                          ImgCreateOptions options) {
        int topPadding = options.getTopPadding();
        int bottomPadding = options.getBottomPadding();

        g2d.setFont(options.getFont());
        FontMetrics fontMetrics = g2d.getFontMetrics();

        // 实际填充内容的高度， 需要排除上下间距
        int contentH = options.getImgH() - options.getTopPadding() - options.getBottomPadding();
        String[] strs = CalculateHelper.splitVerticalStr(content, contentH, g2d.getFontMetrics());


        int fontSize = options.getFont().getSize();
        int fontWidth = fontSize + options.getLinePadding();
        if (options.getDrawStyle() == ImgCreateOptions.DrawStyle.VERTICAL_RIGHT) { // 从右往左绘制时，偏移量为负
            fontWidth = -fontWidth;
        }


        g2d.setColor(options.getFontColor());

        int lastX = x, lastY, startY, tmpCharOffsetX = 0;
        int count = 0;
        for (String tmp : strs) {
            lastY = 0;
            startY = CalculateHelper.calOffsetY(topPadding, bottomPadding, options.getImgH(),
                    fontMetrics.stringWidth(tmp) + fontMetrics.getDescent() * (tmp.length() - 1), options.getAlignStyle())
                    + fontMetrics.getAscent();

            for (int i = 0; i < tmp.length() && count + i <= endIndex; i++) {
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


            count += tmp.length();
            if (count > endIndex) {
                break;
            }
        }

        return lastX + tmpCharOffsetX;
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
        Graphics2D g2d = GraphicUtil.getG2d(source);
        int w = Math.min(dest.getWidth(), options.getImgW() - options.getLeftPadding() - options.getRightPadding());
        int h = w * dest.getHeight() / dest.getWidth();

        int x = CalculateHelper.calOffsetX(options.getLeftPadding(),
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
        Graphics2D g2d = GraphicUtil.getG2d(source);
        int h = Math.min(dest.getHeight(), options.getImgH() - options.getTopPadding() - options.getBottomPadding());
        int w = h * dest.getWidth() / dest.getHeight();

        int y = CalculateHelper.calOffsetY(options.getTopPadding(),
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
        String[] strs = CalculateHelper.splitStr(content, lineLen, g2d.getFontMetrics());


        g2d.setColor(options.getFontColor());
        int index = 0;
        int x;
        for (String tmp : strs) {
            x = CalculateHelper.calOffsetX(leftPadding, rightPadding, w, g2d.getFontMetrics().stringWidth(tmp), options.getAlignStyle());
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
        String[] strs = CalculateHelper.splitVerticalStr(content, contentH, g2d.getFontMetrics());


        int fontSize = options.getFont().getSize();
        int fontWidth = fontSize + options.getLinePadding();
        if (options.getDrawStyle() == ImgCreateOptions.DrawStyle.VERTICAL_RIGHT) { // 从右往左绘制时，偏移量为负
            fontWidth = -fontWidth;
        }


        g2d.setColor(options.getFontColor());

        int lastX = x, lastY, startY, tmpCharOffsetX=0;
        for (String tmp : strs) {
            lastY = 0;
            startY = CalculateHelper.calOffsetY(topPadding, bottomPadding, options.getImgH(),
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
     * 图片旋转
     * @param source
     * @param rotate
     * @return
     */
    public static BufferedImage rotateImg(BufferedImage source, int rotate) {
        if(rotate % 360 ==0) {
            return source;
        }

        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.toRadians(rotate), source.getWidth() >> 1, source.getHeight() >> 1);

        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(source, null);
    }
}
