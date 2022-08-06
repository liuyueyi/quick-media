package com.github.hui.quick.plugin.qrcode.v3.req;


import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.PicStyle;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResourcePool;
import com.github.hui.quick.plugin.qrcode.v3.helper.SvgHelper;

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
            return resourcePool.addSource(1, 1, pre).build().addSource(1, 1, bg).setMiss(0, 0).build().over();
        } else if (pre != null) {
            return resourcePool.addSource(1, 1, pre).build().over();
        } else if (bg != null) {
            return resourcePool.addSource(1, 1, bg).setMiss(0, 0).build().over();
        }
        return this;
    }

    /**
     * 当只有一个二维码信息点的资源图时，使用这个方法，可以有效减少设置的复杂性
     *
     * @param pre
     * @return
     */
    public DrawOptions setRenderResource(QrResource pre) {
        return resourcePool.addSource(1, 1, pre).build().over();
    }

    /**
     * 添加资源, 要求必须存在一个1x1的兜底资源位
     *
     * @param resource
     * @return
     */
    public QrResourcePool.QrResourcesDecorate newRenderResource(QrResource resource) {
        return resourcePool.addSource(1, 1, resource);
    }

    /**
     * 输入svg模板，主要分两块 defs + symbol；用于减轻一个一个设置svg symbol的复杂性
     * svg 模板规则，一个实例demo如下：
     * - defs 标签：内部为预定义的标签，供其他的symbol使用
     * - symbol 标签组：每个symbol要求必须存在 viewBox，读取其中的width, height；若存在width, height属性，则直接取width，height值
     *  - 取所有的symbol中的 width, height的最小值作为 1个qrdot 的基本单位
     *  - 每个symbol的占位  col  = width / qrdot,   row = height / qrdot
     *     <defs>
     *         <g id="def_HA62">
     *             <path d="M5.74,0.06c4.18-0.11,8.34-0.03,12.53,0c0.83-0.02,1.64,0.13,2.43,0.31c0.07,0.96,0.33,1.92,0.28,2.89c-0.06,1.24,0.18,2.5-0.16,3.72C13.95,7,7.08,7.05,0.26,6.67c-0.36-2.1-0.31-4.23-0.07-6.35C1.98-0.05,3.88,0.11,5.74,0.06" style="fill: #8C4410" />
     *         </g>
     *         <g id="def_pTxe">
     *             <path d="M5.74,0.06c4.18-0.11,8.34-0.03,12.53,0c0.83-0.02,1.64,0.13,2.43,0.31c0.07,0.96,0.33,1.92,0.28,2.89c-0.06,1.24,0.18,2.5-0.16,3.72C13.95,7,7.08,7.05,0.26,6.67c-0.36-2.1-0.31-4.23-0.07-6.35C1.98-0.05,3.88,0.11,5.74,0.06" style="fill: #8C4410" />
     *         </g>
     *     </defs>
     *     <symbol id="symbol_1z" viewBox="0 0 49 49">
     *         <use x="0.4" y="0" xlink:href="#def_HA62" />
     *         <use x="27.6" y="42" xlink:href="#def_HA62" />
     *         <use transform="rotate(90)" x="0.4" y="-49" xlink:href="#def_HA62" />
     *         <use transform="rotate(90)" x="27.6" y="-7" xlink:href="#def_HA62" />
     *         <path d="M17.68,15.8c4.12-0.29,8.25-0.32,12.39-0.26c1.08,0.1,2.51-0.15,3.12,1c0.71,1.36,0.8,2.96,1.08,4.45  c0.08,1.92,0,3.83,0.01,5.75c-0.12,2.45-0.03,5.03-1.1,7.29c-5.11,0.55-10.27,0.64-15.38,0.22c-2.45-5.39-2.48-11.52-1.55-17.25  C16.29,16.3,16.98,15.81,17.68,15.8" style="fill: #8C4410" />
     *         <path d="M16.65,29.41c0.58,0.12,1.16,0.26,1.75,0.4c0.12,0.96,0.25,1.92,0.37,2.88c-0.3,0.1-0.91,0.3-1.21,0.39  C17.31,31.85,16.97,30.62,16.65,29.41z" style="fill: #E29126" />
     *     </symbol>
     *     <symbol id="symbol_1A" viewBox="0 0 49 49">
     *         <use x="0.4" y="0" xlink:href="#def_pTxe" />
     *         <use x="27.6" y="42" xlink:href="#def_pTxe" />
     *         <use transform="rotate(90)" x="0.4" y="-49" xlink:href="#def_pTxe" />
     *         <use transform="rotate(90)" x="27.6" y="-7" xlink:href="#def_pTxe" />
     *         <path d="M17.68,15.8c4.12-0.29,8.25-0.32,12.39-0.26c1.08,0.1,2.51-0.15,3.12,1c0.71,1.36,0.8,2.96,1.08,4.45  c0.08,1.92,0,3.83,0.01,5.75c-0.12,2.45-0.03,5.03-1.1,7.29c-5.11,0.55-10.27,0.64-15.38,0.22c-2.45-5.39-2.48-11.52-1.55-17.25  C16.29,16.3,16.98,15.81,17.68,15.8" style="fill: #8C4410" />
     *         <path d="M16.65,29.41c0.58,0.12,1.16,0.26,1.75,0.4c0.12,0.96,0.25,1.92,0.37,2.88c-0.3,0.1-0.91,0.3-1.21,0.39  C17.31,31.85,16.97,30.62,16.65,29.41z" style="fill: #E29126" />
     *     </symbol>
     * @param svgTemplate
     * @return
     */
    public DrawOptions setSvgTemplates(String svgTemplate) {
        drawStyle = DrawStyle.SVG;
        SvgHelper.svgTemplateParseAndInit(options, svgTemplate);
        return this;
    }

    public QrCodeV3Options complete() {
        if (bgColor == null) bgColor = Color.WHITE;
        if (preColor == null) preColor = Color.BLACK;
        if (drawStyle == null) drawStyle = DrawStyle.RECT;
        if (picStyle == null) picStyle = PicStyle.NORMAL;

        return options;
    }
}
