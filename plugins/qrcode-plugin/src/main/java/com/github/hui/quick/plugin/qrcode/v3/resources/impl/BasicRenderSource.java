package com.github.hui.quick.plugin.qrcode.v3.resources.impl;

import com.github.hui.quick.plugin.qrcode.v3.canvas.GraphicQrCanvas;
import com.github.hui.quick.plugin.qrcode.v3.canvas.QrCanvas;
import com.github.hui.quick.plugin.qrcode.v3.canvas.StrQrCanvas;
import com.github.hui.quick.plugin.qrcode.v3.canvas.SvgQrCanvas;
import com.github.hui.quick.plugin.qrcode.v3.options.source.SourceOptions;
import com.github.hui.quick.plugin.qrcode.v3.resources.RenderSource;
import com.github.hui.quick.plugin.qrcode.v3.templates.svg.SvgTag;
import com.github.hui.quick.plugin.qrcode.v3.templates.svg.TextSvgTag;

import java.awt.*;
import java.util.List;

/**
 * @author
 * @date 2022/6/10
 */
public class BasicRenderSource<T> implements RenderSource<T> {
    protected SourceOptions sourceOptions;

    public BasicRenderSource(SourceOptions sourceOptions) {
        this.sourceOptions = sourceOptions;
    }

    @Override
    public <K> void render(QrCanvas canvas, int x, int y, int w, int h) {
        if (canvas instanceof GraphicQrCanvas) {
            qrImgRender(((GraphicQrCanvas) canvas).getG2d(), x, y, w, h);
        } else if (canvas instanceof StrQrCanvas) {
            qrTxtRender(((StrQrCanvas) canvas).getTexts(), x, y, w, h);
        } else if (canvas instanceof SvgQrCanvas) {
            qrSvgRender(((SvgQrCanvas) canvas).getSvgContext(), x, y, w, h);
        } else {
            qrExternalRender(canvas, x, y, w, h);
        }
    }

    public void qrImgRender(Graphics2D g2d, int x, int y, int w, int h) {
        g2d.setColor(sourceOptions.getColor());
        g2d.fillRect(x, y, w, h);
    }

    public void qrTxtRender(char[][] context, int x, int y, int w, int h) {
        char ch = '⬛';
        for (int i = x; i < w; i++) {
            for (int j = y; j < h; h++) {
                context[i][j] = ch;
            }
        }
    }

    public void qrSvgRender(List<SvgTag> svgList, int x, int y, int w, int h) {
        TextSvgTag svgTag = new TextSvgTag().setText("⬛");
        svgTag.setX(x).setY(y).setW(w).setH(h).setColor(sourceOptions.getColor());
        svgList.add(svgTag);
    }

    public <K> void qrExternalRender(QrCanvas canvas, int x, int y, int w, int h) {
        throw new UnsupportedOperationException("render source not support for " + canvas.getClass());
    }

    @Override
    public T getSource() {
        return null;
    }

    @Override
    public SourceOptions getSourceOptions() {
        return sourceOptions;
    }
}
