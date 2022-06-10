package com.github.hui.quick.plugin.qrcode.v3.resources;

import com.github.hui.quick.plugin.qrcode.v3.options.SourceOptions;
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
public class ImgRenderSource extends BasicRenderResource<BufferedImage> {
    /**
     * image source
     */
    protected BufferedImage image;

    private String imgSrc;

    /**
     * 图片的边角弧度 （0 - 1)
     * the corner radian of image (0 - 1)
     * 0 -> corner radian = 90°
     * 1 -> circle image
     */
    protected float radius;

    /**
     * 图片透不明度 （0 - 1）
     * opacity of image (0 - 1)
     * 0 transparency
     * 1 opacity
     */
    private float opacity;

    public ImgRenderSource(SourceOptions sourceOptions) {
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

    @Override
    public void qrTxtRender(char[][] context, int x, int y, int w, int h) {
    }

    @Override
    public void qrSvgRender(List<SvgTag> svgList, int x, int y, int w, int h) {
        ImgSvgTag imgSvgTag = new ImgSvgTag();
        imgSvgTag.setImg(imgSrc).setX(x).setW(w).setY(y).setH(h);
        svgList.add(imgSvgTag);
    }
}

