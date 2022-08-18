package com.github.hui.quick.plugin.qrcode.v3.entity.render;

import com.github.hui.quick.plugin.qrcode.v3.constants.RenderFunc;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.v3.entity.svg.SvgTemplate;

import java.awt.*;

/**
 * 二维矩阵渲染
 *
 * @author YiHui
 * @date 2022/7/20
 */
public class RenderDot {
    protected int x, y;

    protected int size;
    /**
     * 资源类型 0: 探测图形  1: 背景点  2: 信息点
     */
    protected int type;

    protected QrResource resource;

    public int getX() {
        return x;
    }

    public RenderDot setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public RenderDot setY(int y) {
        this.y = y;
        return this;
    }

    public int getType() {
        return type;
    }

    public QrResource getResource() {
        return resource;
    }

    public RenderDot setResource(QrResource resource) {
        this.resource = resource;
        return this;
    }

    public int getSize() {
        return size;
    }

    public RenderDot setSize(int size) {
        this.size = size;
        return this;
    }

    public void renderGeometry(Graphics2D g2d, RenderFunc.GeometryDrawFunc imgFunc) {
        imgFunc.draw(g2d, x, y, size, size);
    }

    public void renderImg(Graphics2D g2d, RenderFunc.ImgDrawFunc drawFunc) {
        drawFunc.draw(g2d, resource.getImg(), x, y, size, size);
    }

    public void renderTxt(Graphics2D g2d, RenderFunc.TxtImgDrawFunc drawFunc) {
        Font oldFont = g2d.getFont();
        if (oldFont.getSize() != size) g2d.setFont(resource.getFont(size));
        drawFunc.draw(g2d, resource.getText(), x, y, size);
        g2d.setFont(oldFont);

    }

    public void renderSvg(SvgTemplate svg, RenderFunc.SvgDrawFunc drawFunc) {
        drawFunc.draw(svg, resource == null ? null : resource.getSvgInfo(), x, y, size, size);
    }

    @Override
    public String toString() {
        return "RenderDot{" + "x=" + x + ", y=" + y + ", size=" + size + ", type=" + type + ", resource=" + resource + '}';
    }
}