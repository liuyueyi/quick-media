package com.github.hui.quick.plugin.qrcode.v3.entity.render;

import com.github.hui.quick.plugin.qrcode.v3.constants.RenderDotType;
import com.github.hui.quick.plugin.qrcode.v3.constants.RenderFunc;
import com.github.hui.quick.plugin.qrcode.v3.entity.svg.SvgTemplate;

import java.awt.*;

/**
 * 二维矩阵渲染
 *
 * @author YiHui
 * @date 2022/7/20
 */
public class PreRenderDot extends RenderDot {
    private int h, w;

    public PreRenderDot() {
        this.type = RenderDotType.PRE.getType();
    }

    public int getH() {
        return h;
    }

    public PreRenderDot setH(int h) {
        this.h = h;
        return this;
    }

    public int getW() {
        return w;
    }

    public PreRenderDot setW(int w) {
        this.w = w;
        return this;
    }

    private int w() {
        return w * size;
    }

    private int h() {
        return h * size;
    }

    @Override
    public void renderGeometry(Graphics2D g2d, RenderFunc.GeometryDrawFunc imgFunc) {
        imgFunc.draw(g2d, x, y, w(), h());
    }

    @Override
    public void renderImg(Graphics2D g2d, RenderFunc.ImgDrawFunc drawFunc) {
        drawFunc.draw(g2d, resource.getImg(), x, y, w(), h());
    }

    @Override
    public void renderTxt(Graphics2D g2d, RenderFunc.TxtImgDrawFunc drawFunc) {
        Font oldFont = g2d.getFont();
        if (oldFont.getSize() != size) g2d.setFont(resource.getFont(size));
        drawFunc.draw(g2d, resource.getText(), x, y, h * size);
        g2d.setFont(oldFont);
    }

    @Override
    public void renderSvg(SvgTemplate svg, RenderFunc.SvgDrawFunc drawFunc) {
        drawFunc.draw(svg, resource == null ? null : resource.getSvgInfo(), x, y, w(), h());
    }

    @Override
    public String toString() {
        return "PreRenderDot{" +
                "h=" + h +
                ", w=" + w +
                ", x=" + x +
                ", y=" + y +
                ", size=" + size +
                ", type=" + type +
                ", resource=" + resource +
                '}';
    }
}