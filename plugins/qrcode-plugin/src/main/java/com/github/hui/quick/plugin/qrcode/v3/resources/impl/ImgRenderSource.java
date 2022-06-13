package com.github.hui.quick.plugin.qrcode.v3.resources.impl;

import com.github.hui.quick.plugin.qrcode.v3.options.source.ImgSourceOptions;
import com.github.hui.quick.plugin.qrcode.v3.templates.svg.ImgSvgTag;
import com.github.hui.quick.plugin.qrcode.v3.templates.svg.SvgTag;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * image render source
 *
 * @author yihui
 * @date 2022/6/10
 */
public class ImgRenderSource extends BasicRenderSource<BufferedImage> {
    /**
     * image source
     */
    protected BufferedImage image;

    public ImgRenderSource(ImgSourceOptions sourceOptions) {
        super(sourceOptions);
    }

    @Override
    public BufferedImage getSource() {
        return image;
    }

    @Override
    public void qrImgRender(Graphics2D g2d, int x, int y, int w, int h) {
        g2d.drawImage(image.getScaledInstance(w, h, Image.SCALE_SMOOTH), x, y, null);
    }

    /**
     * 文字二维码不支持图片
     *
     * @param context
     * @param x
     * @param y
     * @param w
     * @param h
     */
    @Override
    public void qrTxtRender(char[][] context, int x, int y, int w, int h) {
        throw new UnsupportedOperationException("text qrcode not support image sources!");
    }

    @Override
    public void qrSvgRender(List<SvgTag> svgList, int x, int y, int w, int h) {
        ImgSvgTag imgSvgTag = new ImgSvgTag();
        imgSvgTag.setImg(getSourceOptions().getImgSrc()).setX(x).setW(w).setY(y).setH(h);
        svgList.add(imgSvgTag);
    }

    @Override
    public ImgSourceOptions getSourceOptions() {
        return (ImgSourceOptions) sourceOptions;
    }
}

