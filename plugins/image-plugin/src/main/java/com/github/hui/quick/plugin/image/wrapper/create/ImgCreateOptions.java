package com.github.hui.quick.plugin.image.wrapper.create;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yihui on 2017/8/16.
 */
@Getter
@Setter
@ToString
public class ImgCreateOptions {

    public static final Font DEFAULT_FONT = new Font("宋体", Font.PLAIN, 18);
    ;

    /**
     * 绘制的背景图
     */
    private BufferedImage bgImg;


    /**
     * 生成图片的宽
     */
    private Integer imgW;


    /**
     * 生成图片的高
     */
    private Integer imgH;


    private Font font = DEFAULT_FONT;


    /**
     * 字体色
     */
    private Color fontColor = Color.BLACK;


    /**
     * 左边距
     */
    private int leftPadding;

    /**
     * 右边距
     */
    private int rightPadding;

    /**
     * 上边距
     */
    private int topPadding;

    /**
     * 底边距
     */
    private int bottomPadding;

    /**
     * 行距
     */
    private int linePadding;


    /**
     * 对齐方式
     *
     * 水平绘制时: 左对齐，居中， 右对齐
     * 垂直绘制时: 上对齐，居中，下对齐
     *
     */
    private AlignStyle alignStyle;


    /**
     * 文本绘制方式， 水平or垂直
     */
    private DrawStyle drawStyle;


    /**
     * 对齐方式
     */
    public enum AlignStyle {
        LEFT,
        CENTER,
        RIGHT,
        TOP,
        BOTTOM
        ;


        private static Map<String, AlignStyle> map = new HashMap<>();

        static {
            for (AlignStyle style : AlignStyle.values()) {
                map.put(style.name(), style);
            }
        }


        public static AlignStyle getStyle(String name) {
            name = name.toUpperCase();
            if (map.containsKey(name)) {
                return map.get(name);
            }

            return LEFT;
        }
    }


    /**
     * 文本绘制方式， 水平绘制，or 垂直绘制
     */
    public enum DrawStyle {
        // 垂直绘制，从左到右
        VERTICAL_LEFT,
        // 垂直绘制，从右到左
        VERTICAL_RIGHT,
        // 水平绘制
        HORIZONTAL;


        private static Map<String, DrawStyle> map = new HashMap<>();

        static {
            for (DrawStyle style : DrawStyle.values()) {
                map.put(style.name(), style);
            }
        }


        public static DrawStyle getStyle(String name) {
            name = name.toUpperCase();
            if (map.containsKey(name)) {
                return map.get(name);
            }

            return HORIZONTAL;
        }
    }
}
