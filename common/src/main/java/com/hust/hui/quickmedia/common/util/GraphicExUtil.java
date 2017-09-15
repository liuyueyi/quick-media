package com.hust.hui.quickmedia.common.util;

import com.hust.hui.quickmedia.common.image.ImgCreateOptions;

import java.awt.*;

/**
 * Created by yihui on 2017/9/15.
 */
public class GraphicExUtil {


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
        String[] strs = GraphicUtil.splitStr(content, lineLen, g2d.getFontMetrics());


        g2d.setColor(options.getFontColor());
        int index = 0;
        int x, count = 0;
        int tempEndIndex;
        for (String tmp : strs) {
            x = GraphicUtil.calOffsetX(leftPadding, rightPadding, w, g2d.getFontMetrics().stringWidth(tmp), options.getAlignStyle());

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
        String[] strs = GraphicUtil.splitVerticalStr(content, contentH, g2d.getFontMetrics());


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
            startY = GraphicUtil.calOffsetY(topPadding, bottomPadding, options.getImgH(),
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

}
