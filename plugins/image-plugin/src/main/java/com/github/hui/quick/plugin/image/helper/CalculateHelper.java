package com.github.hui.quick.plugin.image.helper;

import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;

import java.awt.*;

/**
 * Created by yihui on 2018/3/26.
 */
public class CalculateHelper {

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
