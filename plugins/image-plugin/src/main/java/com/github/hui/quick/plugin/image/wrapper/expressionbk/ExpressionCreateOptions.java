package com.github.hui.quick.plugin.image.wrapper.expressionbk;

import lombok.Data;

import java.awt.*;
import java.util.List;


/**
 * Created by yihui on 2017/9/14.
 */
@Data
public class ExpressionCreateOptions {
    /**
     * 四周的边框
     */
    private Integer padding = 40;

    /**
     * 生成表情图的宽
     */
    private Integer w;


    /**
     * 生成表情图的高
     */
    private Integer h;


    /**
     * 字体
     */
    private Font font;


    private Integer logoW;

    private Integer logoH;

    private int logoX;

    private int logoY;

    /**
     * 背景色
     */
    private Color bgColor = Color.WHITE;


    /**
     * 每一帧的内容
     */
    private List<ExpressionFrameEntity> frameList;


    /**
     * 延迟时间
     */
    private int delay = 100;
}
