package com.github.hui.quick.plugin.qrcode.v3.canvas;

import com.github.hui.quick.plugin.base.GraphicUtil;
import com.github.hui.quick.plugin.qrcode.v3.resources.RenderSource;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图片版二维码 画布
 *
 * @author yihui
 * @date 2022/6/10
 */
public class GraphicQrCanvas implements QrCanvas {
    private BufferedImage qrImg;

    private Graphics2D g2d;

    public GraphicQrCanvas(int w, int h) {
        qrImg = GraphicUtil.createImg(w, h, null);
        g2d = GraphicUtil.getG2d(qrImg);
    }

    public GraphicQrCanvas(BufferedImage qrImg) {
        this.qrImg = qrImg;
        g2d = GraphicUtil.getG2d(qrImg);
    }

    public BufferedImage getQrImg() {
        return qrImg;
    }

    public Graphics2D getG2d() {
        return g2d;
    }

    @Override
    public void draw(RenderSource<?> resource, int x, int y, int w, int h) {
        resource.render(this, x, y, w, h);
    }

    @Override
    public BufferedImage output() {
        g2d.dispose();
        return qrImg;
    }
}
