package com.github.hui.quick.plugin.qrcode.v3.req;


import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.PicStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.TxtMode;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.v3.entity.RenderResourcesV3;

import java.awt.*;

/**
 * 绘制二维码的配置信息
 */
public class DrawOptions {
    private QrCodeV3Options options;

    /**
     * 着色颜色
     */
    private Color preColor;

    /**
     * 背景颜色
     */
    private Color bgColor;

    /**
     * 绘制样式
     */
    private DrawStyle drawStyle;
    /**
     * 生成文字二维码时的字体
     */
    private String fontName;

    /**
     * 文字二维码渲染模式
     */
    private TxtMode txtMode;

    /**
     * 字体样式
     * <p>
     * {@link Font#PLAIN} 0
     * {@link Font#BOLD}  1
     * {@link Font#ITALIC} 2
     */
    private int fontStyle;

    /**
     * true 时表示支持对相邻的着色点进行合并处理 （即用一个大图来绘制相邻的两个着色点）
     * <p>
     * 说明： 三角形样式关闭该选项，因为留白过多，对识别有影响
     */
    private boolean enableScale;

    /**
     * 图片透明处填充，true则表示透明处用bgColor填充； false则透明处依旧透明
     */
    private boolean transparencyBgFill = true;

    /**
     * 生成二维码的图片样式，一般来讲不推荐使用圆形，默认为normal；如果是圆角，则可以配套设置 cornerRadius
     */
    private PicStyle picStyle;

    /**
     * 圆角的弧度，默认为 1 / 8
     */
    private Float cornerRadius;

    /**
     * 渲染资源
     */
    private RenderResourcesV3 resources;

    public DrawOptions(QrCodeV3Options options) {
        this.options = options;
        resources = RenderResourcesV3.create(this);
    }

    public Color getPreColor() {
        return preColor;
    }

    public DrawOptions setPreColor(Color preColor) {
        this.preColor = preColor;
        return this;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public DrawOptions setBgColor(Color bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    public DrawStyle getDrawStyle() {
        return drawStyle;
    }

    public DrawOptions setDrawStyle(DrawStyle drawStyle) {
        this.drawStyle = drawStyle;
        return this;
    }

    public String getFontName() {
        return fontName;
    }

    public DrawOptions setFontName(String fontName) {
        this.fontName = fontName;
        return this;
    }

    public TxtMode getTxtMode() {
        return txtMode;
    }

    public DrawOptions setTxtMode(TxtMode txtMode) {
        this.txtMode = txtMode;
        return this;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public DrawOptions setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
        return this;
    }

    public boolean isEnableScale() {
        return enableScale;
    }

    public DrawOptions setEnableScale(boolean enableScale) {
        this.enableScale = enableScale;
        return this;
    }

    public boolean isTransparencyBgFill() {
        return transparencyBgFill;
    }

    public DrawOptions setTransparencyBgFill(boolean transparencyBgFill) {
        this.transparencyBgFill = transparencyBgFill;
        return this;
    }

    public PicStyle getPicStyle() {
        return picStyle;
    }

    public DrawOptions setPicStyle(PicStyle picStyle) {
        this.picStyle = picStyle;
        return this;
    }

    public Float getCornerRadius() {
        return cornerRadius;
    }

    public DrawOptions setCornerRadius(Float cornerRadius) {
        this.cornerRadius = cornerRadius;
        return this;
    }

    public RenderResourcesV3 getResources() {
        return resources;
    }

    /**
     * 只有一个图片的场景，用这个接口
     *
     * @param pre 1点对应的图
     * @param bg  0点对应的图
     * @return
     */
    public DrawOptions setRenderResource(QrResource pre, QrResource bg) {
        resources.addSource(1, 1, pre).build().addSource(1, 1, bg).setMiss(0, 0).build();
        return this;
    }

    /**
     * 添加资源
     *
     * @param resource
     * @return
     */
    public RenderResourcesV3.RenderSource newRenderResource(int row, int col, QrResource resource) {
        return resources.addSource(row, col, resource);
    }

    public QrCodeV3Options complete() {
        if (bgColor == null) bgColor = Color.WHITE;
        if (preColor == null) preColor = Color.BLACK;
        if (drawStyle == null) drawStyle = DrawStyle.RECT;
        if (picStyle == null) picStyle = PicStyle.NORMAL;

        return options;
    }
}
