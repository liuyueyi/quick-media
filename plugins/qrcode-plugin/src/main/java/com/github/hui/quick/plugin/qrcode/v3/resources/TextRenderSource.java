package com.github.hui.quick.plugin.qrcode.v3.resources;

import com.github.hui.quick.plugin.qrcode.v3.canvas.GraphicQrCanvas;
import com.github.hui.quick.plugin.qrcode.v3.canvas.QrCanvas;
import com.github.hui.quick.plugin.qrcode.v3.canvas.StrQrCanvas;
import com.github.hui.quick.plugin.qrcode.v3.canvas.SvgQrCanvas;
import com.github.hui.quick.plugin.qrcode.v3.options.SourceOptions;
import com.github.hui.quick.plugin.qrcode.v3.templates.svg.SvgTag;
import com.github.hui.quick.plugin.qrcode.v3.templates.svg.TextSvgTag;

import java.awt.*;
import java.util.List;

/**
 * @author
 * @date 2022/6/10
 */
public class TextRenderSource extends BasicRenderResource<String> {
    private String text;
    private int index;
    /**
     * 0 sequence 1 random
     */
    private int style;

    public TextRenderSource(SourceOptions sourceOptions) {
        super(sourceOptions);
        index = 0;
    }

    @Override
    public String getSource() {
        if (style == 0) {
            String out = String.valueOf(text.charAt(index));
            index = (index + 1) % text.length();
            return out;
        } else {
            index = (int) (Math.random() * text.length());
            return String.valueOf(text.charAt(index));
        }
    }

    @Override
    public <K> void render(QrCanvas canvas, int x, int y, int w, int h) {
        if (canvas instanceof GraphicQrCanvas) {
            qrImgRender(((GraphicQrCanvas) canvas).getG2d(), x, y, w, h);
        } else if (canvas instanceof StrQrCanvas) {
            qrTxtRender(((StrQrCanvas) canvas).getTexts(), x, y, w, h);
        } else if (canvas instanceof SvgQrCanvas) {
            qrSvgRender(((SvgQrCanvas) canvas).getSvgContext(), x, y, w, h);
        }
    }

    @Override
    public void qrImgRender(Graphics2D g2d, int x, int y, int w, int h) {
        g2d.setColor(sourceOptions.getColor());
        g2d.setFont(sourceOptions.getFont());
        g2d.drawString(getSource(), x, y);
    }

    @Override
    public void qrTxtRender(char[][] context, int x, int y, int w, int h) {
        for (int i = x; i < w; i++) {
            for (int j = y; j < h; h++) {
                context[i][j] = getSource().charAt(0);
            }
        }
    }

    @Override
    public void qrSvgRender(List<SvgTag> svgList, int x, int y, int w, int h) {
        TextSvgTag svgTag = new TextSvgTag();
        svgTag.setX(x).setY(y).setW(w).setH(h).setColor(sourceOptions.getColor());
        svgList.add(svgTag);
    }
}
