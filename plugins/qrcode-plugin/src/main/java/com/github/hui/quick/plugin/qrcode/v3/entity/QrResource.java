package com.github.hui.quick.plugin.qrcode.v3.entity;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.base.gif.GifDecoder;
import com.github.hui.quick.plugin.base.gif.GifHelper;
import com.github.hui.quick.plugin.qrcode.constants.QuickQrUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.PicStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.QrType;
import com.github.hui.quick.plugin.qrcode.v3.constants.TxtMode;
import com.github.hui.quick.plugin.qrcode.v3.draw.IDrawing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YiHui
 * @date 2022/7/20
 */
public class QrResource {
    /**
     * 动态背景图
     */
    private GifDecoder gif;

    /**
     * 背景图
     */
    private BufferedImage img;

    /**
     * 图样式
     */
    private PicStyle picStyle = PicStyle.NORMAL;

    /**
     * 图片的圆角比例，默认为原图的 1/8
     */
    private float cornerRadius = 1 / 8.0f;

    /**
     * 文字
     */
    private String text;

    /**
     * 文字二维码渲染模式
     */
    private TxtMode txtMode;

    /**
     * 生成文字二维码时的字体
     */
    private String fontName;

    /**
     * 字体样式
     * <p>
     * {@link Font#PLAIN} 0
     * {@link Font#BOLD}  1
     * {@link Font#ITALIC} 2
     */
    private int fontStyle;

    /**
     * 文字顺序处理是
     */
    private AtomicInteger indexCnt;

    /**
     * svg tag
     */
    private String svg;

    /**
     * svg tag id
     */
    private String svgId;

    /**
     * 支持为每个资源位设置特殊的绘制方式
     */
    private IDrawing drawStyle;

    /**
     * 绘制颜色
     */
    private Color drawColor;

    public QrResource() {
    }

    public QrResource(String resource) {
        resource = resource.trim();
        if (resource.startsWith(SVG_START_TAG)) {
            setSvg(resource);
        } else {
            setImg(resource);
        }
    }

    public GifDecoder getGif() {
        return gif;
    }

    public QrResource setGif(GifDecoder gif) {
        this.gif = gif;
        return this;
    }

    public QrResource setGif(String gif) {
        try {
            this.gif = GifHelper.loadGif(gif);
            return this;
        } catch (Exception e) {
            throw new IllegalArgumentException("illegal gif img: " + gif);
        }
    }

    public BufferedImage getImg() {
        return img;
    }

    public QrResource setImg(BufferedImage img) {
        this.img = img;
        return this;
    }

    public QrResource setImg(String img) {
        try {
            if (img.toLowerCase().endsWith(QrType.GIF.getSuffix())) {
                // 如果传入的是gif图，则转到setGif
                return setGif(img);
            }

            return setImg(ImageLoadUtil.getImageByPath(img));
        } catch (Exception e) {
            throw new IllegalArgumentException("can't load img: " + img);
        }
    }

    public String getText() {
        if (txtMode == null) {
            setTxtMode(TxtMode.FULL);
        }
        return txtMode.txt(text, indexCnt);
    }

    public QrResource setText(String text) {
        this.text = text;
        return this;
    }

    public TxtMode getTxtMode() {
        return txtMode;
    }

    public QrResource setTxtMode(TxtMode txtMode) {
        this.txtMode = txtMode;
        if (txtMode == TxtMode.ORDER) indexCnt = new AtomicInteger(0);
        return this;
    }

    public QrResource setFontName(String fontName) {
        this.fontName = fontName;
        return this;
    }

    public QrResource setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
        return this;
    }

    public Font getFont(int size) {
        if (fontName == null) fontName = QuickQrUtil.DEFAULT_FONT_NAME;
        return QuickQrUtil.font(fontName, fontStyle, size);
    }

    public AtomicInteger getIndexCnt() {
        return indexCnt;
    }

    public QrResource setIndexCnt(AtomicInteger indexCnt) {
        this.indexCnt = indexCnt;
        return this;
    }

    public String getSvg() {
        return svg;
    }

    private static final String SVG_START_TAG = "<symbol ";
    private static final String SVG_ID_TAG = " id=\"";

    public QrResource setSvg(String svg, boolean ignoreCheck) {
        svg = svg.trim();
        this.svg = svg;
        if (!ignoreCheck && !svg.startsWith(SVG_START_TAG)) {
            throw new IllegalArgumentException("svg只接受<symbol/>, <defs/>定义资源!");
        }

        if (svgId == null) {
            int index = svg.indexOf(SVG_ID_TAG);
            if (index >= 0) {
                index += SVG_ID_TAG.length();
                svgId = svg.substring(index, svg.indexOf("\"", index));
            } else {
                // 当没有id时，默认分配一个
                svgId = UUID.randomUUID().toString();
                this.svg = SVG_START_TAG + SVG_ID_TAG + svgId + "\" " + svg.substring(SVG_START_TAG.length());
            }
        }
        if (this.drawStyle == null) {
            // 设置svg之后，默认更新对应的绘制样式为SVG
            setDrawStyle(DrawStyle.SVG);
        }
        return this;
    }

    public QrResource setSvg(String svg) {
        return this.setSvg(svg, false);
    }

    public QrResource setSvg(String id, String svg) {
        this.svgId = id;
        int index = svg.indexOf(SVG_ID_TAG);
        if (index >= 0) {
            // 若id存在，则使用给定的进行替换
            index += SVG_ID_TAG.length();
            int end = svg.indexOf("\"", index);
            this.svg = svg.substring(0, index) + id + svg.substring(end);
        } else {
            // 需要给svg模板添加id
            this.svg = SVG_START_TAG + SVG_ID_TAG + svgId + "\" " + svg.substring(SVG_START_TAG.length());
        }

        if (this.drawStyle == null) {
            // 设置svg之后，默认更新对应的绘制样式为SVG
            setDrawStyle(DrawStyle.SVG);
        }
        return this;
    }

    /**
     * 如果是symbol标签，则返回svgId
     * 如果是svg 样式模板，则直接返回
     * 如果是文字，注意一次只返回一个
     *
     * @return
     */
    public String getSvgInfo() {
        if (svgId != null) return svgId;
        if (this.svg != null) return svg;
        if (text != null) {
            if (txtMode == null) txtMode = TxtMode.ORDER;
            if (txtMode == TxtMode.ORDER && indexCnt == null) indexCnt = new AtomicInteger(0);
            return txtMode.txt(text, indexCnt);
        }
        return null;
    }

    public String getSvgId() {
        return svgId;
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public QrResource setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        return this;
    }

    public PicStyle getPicStyle() {
        if (picStyle == null) picStyle = PicStyle.NORMAL;
        return picStyle;
    }

    public QrResource setPicStyle(PicStyle picStyle) {
        this.picStyle = picStyle;
        return this;
    }

    public IDrawing getDrawStyle() {
        return drawStyle;
    }

    public QrResource setDrawStyle(IDrawing drawStyle) {
        this.drawStyle = drawStyle;
        return this;
    }

    public Color getDrawColor() {
        return drawColor;
    }

    public QrResource setDrawColor(Color drawColor) {
        this.drawColor = drawColor;
        return this;
    }

    public QrResource setDrawColor(int color) {
        this.drawColor = ColorUtil.int2color(color);
        return this;
    }

    public QrResource setDrawColor(String htmlColor) {
        this.drawColor = ColorUtil.html2color(htmlColor);
        return this;
    }

    public BufferedImage processImg() {
        if (picStyle == null) picStyle = PicStyle.NORMAL;
        return picStyle.process(img, (int) (Math.min(img.getWidth(), img.getHeight()) * cornerRadius));
    }
}
