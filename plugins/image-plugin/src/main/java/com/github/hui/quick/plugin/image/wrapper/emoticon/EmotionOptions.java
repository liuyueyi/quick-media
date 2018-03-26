package com.github.hui.quick.plugin.image.wrapper.emoticon;

import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by yihui on 2017/9/19.
 */
@Data
public class EmotionOptions {

    private int w;

    private int h;

    /**
     * 文字内容
     */
    private String[] content;

    /**
     * 文字对应占用的高度or宽度
     */
    private int contentSize;

    private Font font;



    private boolean imgFirst;

    private Color fontColor;


    /**
     * gif 中对应的每一帧图片
     */
    private List<BufferedImage> imgs;

    /**
     * gif 图实际绘制的宽
     */
    private int gifW;

    /**
     * gif 图实际绘制的高
     */
    private int gifH;

    /**
     * gif 图时间
     */
    private int delay;



    private ImgCreateOptions.DrawStyle drawStyle;


    private int leftPadding;

    private int rightPadding;

    private int topPadding;

    private int bottomPadding;

    private int linePadding;
}
