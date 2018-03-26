package com.github.hui.quick.plugin.image.wrapper.expressionbk;

import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 表情包中每一帧的内容
 * Created by yihui on 2017/9/14.
 */
@Data
public class ExpressionFrameEntity {

    private String content;

    private Color fontColor;

    private BufferedImage bg;

    private ImgCreateOptions.DrawStyle drawStyle;

    public ExpressionFrameEntity() {
    }

    public ExpressionFrameEntity(ExpressionFrameEntity entity) {
        this.content = entity.getContent();
        this.fontColor = entity.getFontColor();
        this.bg = entity.getBg();
        this.drawStyle = entity.getDrawStyle();
    }
}
