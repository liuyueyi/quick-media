package com.hust.hui.quickmedia.common.image;

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

    /**
     * 绘制的背景图
     */
    private BufferedImage bgImg;


    /**
     * 生成图片的宽
     */
    private Integer imgW;


    private Font font = new Font("宋体", Font.PLAIN, 18);


    private Color fontColor = Color.BLACK;


    /**
     * 两边边距
     */
    private int leftPadding;

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


    private AlignStyle alignStyle;

    /**
     * 对齐方式
     */
    public enum AlignStyle {
        LEFT,
        CENTER,
        RIGHT;


        private static Map<String, AlignStyle> map = new HashMap<>();

        static {
            for(AlignStyle style: AlignStyle.values()) {
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
}
