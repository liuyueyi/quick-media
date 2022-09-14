package com.github.hui.quick.plugin.qrcode.v3.req;


import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.PicStyle;
import com.github.hui.quick.plugin.qrcode.v3.draw.IDrawing;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResourcePool;

import java.awt.*;

/**
 * 绘制二维码的配置信息
 *
 * @author YiHui
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
    private IDrawing drawStyle;

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
     * 当生成二维码的图片需要做圆角处理时，这个参数控制圆角的弧度，默认为 1 / 8
     */
    private Float cornerRadius;

    /**
     * 渲染资源
     */
    private final QrResourcePool resourcePool;

    public DrawOptions(QrCodeV3Options options) {
        this.options = options;
        resourcePool = QrResourcePool.create(this);
    }

    public Color getPreColor() {
        return preColor;
    }

    public DrawOptions setPreColor(Color preColor) {
        this.preColor = preColor;
        return this;
    }

    public DrawOptions setPreColor(int color) {
        return setPreColor(ColorUtil.int2color(color));
    }

    public Color getBgColor() {
        return bgColor;
    }

    public DrawOptions setBgColor(Color bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    public DrawOptions setBgColor(int color) {
        return setBgColor(ColorUtil.int2color(color));
    }

    public IDrawing getDrawStyle() {
        return drawStyle;
    }

    public DrawOptions setDrawStyle(IDrawing drawStyle) {
        this.drawStyle = drawStyle;
        return this;
    }

    public boolean isEnableScale() {
        return enableScale;
    }

    public DrawOptions setEnableScale(boolean enableScale) {
        this.enableScale = enableScale;
        return this;
    }

    /**
     * @return true 表示资源图片中透明的地方，用背景颜色填充
     */
    public boolean isTransparencyBgFill() {
        return transparencyBgFill;
    }

    /**
     * 图片透明处填充，true则表示透明处用bgColor填充； false则透明处依旧透明
     */
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

    public QrResourcePool getResourcePool() {
        return resourcePool;
    }

    /**
     * 设置全局的资源信息，如svg二维码中的 defs 预定义模板；如二维码图片中文字的全局样式
     *
     * @param resource
     * @return
     */
    public DrawOptions setGlobalResource(QrResource resource) {
        resourcePool.setGlobalResource(resource);
        return this;
    }

    /**
     * 只有一个图片的场景，用这个接口
     *
     * @param pre 1点对应的图
     * @param bg  0点对应的图
     * @return
     */
    public DrawOptions setRenderResource(QrResource pre, QrResource bg) {
        if (pre != null && bg != null) {
            return resourcePool.createSource(1, 1).addResource(pre).build()
                    .createSource(1, 1).addResource(bg).setMiss(0, 0).build()
                    .over();
        } else if (pre != null) {
            return resourcePool.createSource(1, 1).addResource(pre).build().over();
        } else if (bg != null) {
            return resourcePool.createSource(1, 1).addResource(bg).setMiss(0, 0).build().over();
        }
        return this;
    }

    public DrawOptions setRenderResource(String pre, String bg) {
        return setRenderResource(new QrResource(pre), new QrResource(bg));
    }

    /**
     * 当只有一个二维码信息点的资源图时，使用这个方法，可以有效减少设置的复杂性
     *
     * @param pre
     * @return
     */
    public DrawOptions setRenderResource(QrResource pre) {
        return resourcePool.createSource(1, 1).addResource(pre).build().over();
    }

    public DrawOptions setRenderResource(String pre) {
        return setRenderResource(new QrResource(pre));
    }

    /**
     * 添加资源, 要求必须存在一个1x1的兜底资源位，这个方法主要应用于多资源配置的场景
     *
     * @param resource
     * @return
     */
    public QrResourcePool.QrResourcesDecorate newRenderResource(QrResource resource) {
        return newRenderResource(1, 1, resource);
    }

    public QrResourcePool.QrResourcesDecorate newRenderResource(String resource) {
        return newRenderResource(new QrResource(resource));
    }

    public QrResourcePool.QrResourcesDecorate newRenderResource(int width, int height, QrResource resource) {
        return resourcePool.createSource(width, height).addResource(resource);
    }

    public QrResourcePool.QrResourcesDecorate newRenderResource(int width, int height, String resource) {
        return newRenderResource(width, height, new QrResource(resource));
    }

    public QrCodeV3Options complete() {
        if (bgColor == null) bgColor = Color.WHITE;
        if (preColor == null) preColor = Color.BLACK;
        if (drawStyle == null) drawStyle = DrawStyle.RECT;
        if (picStyle == null) picStyle = PicStyle.NORMAL;
        if (cornerRadius == null) cornerRadius = 1 / 8.0f;

        return options;
    }
}
