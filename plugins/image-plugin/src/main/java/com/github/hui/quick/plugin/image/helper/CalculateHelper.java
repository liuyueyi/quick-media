package com.github.hui.quick.plugin.image.helper;

import com.github.hui.quick.plugin.image.util.CIEDE2000;
import com.github.hui.quick.plugin.image.util.StrUtil;
import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
    public static int calOffsetX(int leftPadding, int rightPadding, int width, int strSize,
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
    public static int calOffsetY(int topPadding, int bottomPadding, int height, int strSize,
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
        return doSplitStr(str, lineNum, lineLen, fontMetrics::charWidth);
    }

    /**
     * 竖排文本与横排主要区别在分行的计算以及一个字符的占位高度
     *
     * @param str
     * @param lineLen
     * @param fontMetrics
     * @return
     */
    public static String[] splitVerticalStr(String str, int lineLen, FontMetrics fontMetrics) {
        int l = fontMetrics.getDescent() * (str.length() - 1);
        int lineNum = (int) Math.ceil((fontMetrics.stringWidth(str) + l) / (float) lineLen);

        return doSplitStr(str, lineNum, lineLen,
                character -> fontMetrics.charWidth(character) + fontMetrics.getDescent());
    }

    /**
     * 实现字符串根据每行容纳的长度进行分割
     *
     * @param str             待分割的字符串内容
     * @param lineNum         分割后的行数（不一定完全准确，因此实现逻辑中用List替换了之前的数组，避免越界问题）
     * @param lineLen         每行容纳的长度
     * @param charLenCalcFunc 计算字符站位的方法
     * @return
     */
    private static String[] doSplitStr(String str, int lineNum, int lineLen,
                                       Function<Character, Integer> charLenCalcFunc) {
        if (lineNum == 1) {
            return new String[]{str};
        }

        List<String> ans = new ArrayList<>(lineNum);
        int strLen = str.length();
        // 保存当前行的站位长度
        int currentLineTotalLen = 0;
        // 保存当前行的起始位置
        int currentLineStartIndex = 0;
        int tmpLen;
        for (int i = 0; i < strLen; i++) {
            /**
             * 因为每个文本的宽度不一样，所以需要采用遍历的方式来计算一行最终能容纳多少个字符
             */
            tmpLen = charLenCalcFunc.apply(str.charAt(i));
            currentLineTotalLen += tmpLen;
            if (currentLineTotalLen > lineLen) {
                ans.add(str.substring(currentLineStartIndex, i));
                currentLineStartIndex = i;
                currentLineTotalLen = tmpLen;
            }
        }

        if (currentLineStartIndex < strLen) {
            ans.add(str.substring(currentLineStartIndex));
        }

        return StrUtil.toArray(ans);
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
            totalLine += Math.ceil(
                    (fontMetrics.stringWidth(str) + (str.length() - 1) * fontMetrics.getDescent()) / (float) lineLen);
        }
        return totalLine;
    }

    public static boolean sameColorByThreshold(Color c1, Color c2, float threshold) {
//        int cnt = Math.abs(c1.getRed() - c2.getRed()) > threshold ? 0 : 1;
//        cnt = Math.abs(c1.getGreen() - c2.getGreen()) > threshold ? cnt : cnt + 1;
//        cnt = Math.abs(c1.getBlue() - c2.getBlue()) > threshold ? cnt : cnt + 1;
//        // 只要有两个颜色的差值再与阈值内，则认为颜色相同
//        return cnt == 3;

        return CIEDE2000.calculateCIEDE2000(c1, c2) <= 3;
    }


}
