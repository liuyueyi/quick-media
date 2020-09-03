package com.github.hui.quick.plugin.image.wrapper.expressionbk;

import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * 表情包中每一帧的内容
 * Created by yihui on 2017/9/14.
 */
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public BufferedImage getBg() {
        return bg;
    }

    public void setBg(BufferedImage bg) {
        this.bg = bg;
    }

    public ImgCreateOptions.DrawStyle getDrawStyle() {
        return drawStyle;
    }

    public void setDrawStyle(ImgCreateOptions.DrawStyle drawStyle) {
        this.drawStyle = drawStyle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExpressionFrameEntity that = (ExpressionFrameEntity) o;
        return Objects.equals(content, that.content) && Objects.equals(fontColor, that.fontColor) &&
                Objects.equals(bg, that.bg) && drawStyle == that.drawStyle;
    }

    @Override
    public int hashCode() {

        return Objects.hash(content, fontColor, bg, drawStyle);
    }

    @Override
    public String toString() {
        return "ExpressionFrameEntity{" + "content='" + content + '\'' + ", fontColor=" + fontColor + ", bg=" + bg +
                ", drawStyle=" + drawStyle + '}';
    }
}
