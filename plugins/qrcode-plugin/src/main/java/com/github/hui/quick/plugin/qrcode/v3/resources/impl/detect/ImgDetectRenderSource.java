package com.github.hui.quick.plugin.qrcode.v3.resources.impl.detect;

import com.github.hui.quick.plugin.qrcode.v3.options.source.DetectImgSourceOptions;
import com.github.hui.quick.plugin.qrcode.v3.options.source.DetectSourceOptions;
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
public class ImgDetectRenderSource extends DetectRenderSource<BufferedImage> {
    protected BufferedImage out;
    protected BufferedImage in;
    protected BufferedImage bg;

    public ImgDetectRenderSource(DetectImgSourceOptions sourceOptions) {
        super(sourceOptions);
    }

    @Override
    public BufferedImage getSource() {
        return null;
    }

    public BufferedImage getSource(DetectSourceOptions.DetectSourceArea position) {
        BufferedImage image;
        switch (position) {
            case BG:
                image = bg;
                break;
            case OUT:
                image = out;
                break;
            case IN:
                image = in;
                break;
            default:
                image = null;
        }
        return image;
    }

    @Override
    public void qrImgRender(Graphics2D g2d, DetectSourceOptions.DetectSourceArea position, int x, int y, int w, int h) {
        BufferedImage image = getSource(position);
        if (image == null) {
            super.qrImgRender(g2d, position, x, y, w, h);
        } else {
            g2d.drawImage(image.getScaledInstance(w, h, Image.SCALE_SMOOTH), x, y, null);
        }
    }

    @Override
    public void qrTxtRender(char[][] context, DetectSourceOptions.DetectSourceArea position, int x, int y, int w, int h) {
        throw new UnsupportedOperationException("text qrcode not support image sources!");
    }

    @Override
    public void qrSvgRender(List<SvgTag> svgList, DetectSourceOptions.DetectSourceArea position, int x, int y, int w, int h) {
        BufferedImage image = getSource(position);
        ImgSvgTag imgSvgTag = new ImgSvgTag();
        imgSvgTag.setX(x).setW(w).setY(y).setH(h);
        if (image == null) {
            imgSvgTag.setImg(getSourceOptions().getImgSrc());
        }
        svgList.add(imgSvgTag);
    }

    @Override
    public DetectImgSourceOptions getSourceOptions() {
        return (DetectImgSourceOptions) sourceOptions;
    }
}

