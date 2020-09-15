package com.github.hui.quick.plugin.md.entity;

import org.w3c.dom.Document;

import java.awt.*;
import java.util.Objects;

/**
 * Created by yihui on 2017/9/11.
 */
public class HtmlRenderOptions {

    /**
     * 输出图片的宽
     */
    private Integer w;


    /**
     * 输出图片的高
     */
    private Integer h;


    /**
     * 是否自适应宽
     */
    private boolean autoW;


    /**
     * 是否自适应高
     */
    private boolean autoH;


    /**
     * 输出图片的格式
     */
    private String outType;

    /**
     * html相关内容
     */
    private Document document;


    /**
     * 字体
     */
    private Font font;


    /**
     * 绘制字体颜色
     */
    private String fontColor;

    public Integer getW() {
        return w;
    }

    public void setW(Integer w) {
        this.w = w;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public boolean isAutoW() {
        return autoW;
    }

    public void setAutoW(boolean autoW) {
        this.autoW = autoW;
    }

    public boolean isAutoH() {
        return autoH;
    }

    public void setAutoH(boolean autoH) {
        this.autoH = autoH;
    }

    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HtmlRenderOptions that = (HtmlRenderOptions) o;
        return autoW == that.autoW && autoH == that.autoH && Objects.equals(w, that.w) && Objects.equals(h, that.h) &&
                Objects.equals(outType, that.outType) && Objects.equals(document, that.document) &&
                Objects.equals(font, that.font) && Objects.equals(fontColor, that.fontColor);
    }

    @Override
    public int hashCode() {

        return Objects.hash(w, h, autoW, autoH, outType, document, font, fontColor);
    }

    @Override
    public String toString() {
        return "HtmlRenderOptions{" + "w=" + w + ", h=" + h + ", autoW=" + autoW + ", autoH=" + autoH + ", outType='" +
                outType + '\'' + ", document=" + document + ", font=" + font + ", fontColor='" + fontColor + '\'' + '}';
    }
}
