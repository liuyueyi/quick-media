package com.github.hui.quick.plugin.qrcode.v3.resources.impl.detect;

import com.github.hui.quick.plugin.qrcode.v3.canvas.GraphicQrCanvas;
import com.github.hui.quick.plugin.qrcode.v3.canvas.QrCanvas;
import com.github.hui.quick.plugin.qrcode.v3.canvas.StrQrCanvas;
import com.github.hui.quick.plugin.qrcode.v3.canvas.SvgQrCanvas;
import com.github.hui.quick.plugin.qrcode.v3.options.source.DetectSourceOptions;
import com.github.hui.quick.plugin.qrcode.v3.resources.impl.BasicRenderSource;
import com.github.hui.quick.plugin.qrcode.v3.templates.svg.SvgTag;
import com.github.hui.quick.plugin.qrcode.v3.templates.svg.TextSvgTag;

import java.awt.*;
import java.util.List;

/**
 * @author
 * @date 2022/6/12
 */
public class DetectRenderSource<T> extends BasicRenderSource<T> {
    protected DetectSourceOptions sourceOptions;

    public DetectRenderSource(DetectSourceOptions sourceOptions) {
        super(sourceOptions);
        this.sourceOptions = sourceOptions;
    }

    public void render(QrCanvas canvas, DetectSourceOptions.DetectSourceArea position, int x, int y, int w, int h) {
        if (canvas instanceof GraphicQrCanvas) {
            qrImgRender(((GraphicQrCanvas) canvas).getG2d(), position, x, y, w, h);
        } else if (canvas instanceof StrQrCanvas) {
            qrTxtRender(((StrQrCanvas) canvas).getTexts(), position, x, y, w, h);
        } else if (canvas instanceof SvgQrCanvas) {
            qrSvgRender(((SvgQrCanvas) canvas).getSvgContext(), position, x, y, w, h);
        } else {
            qrExternalRender(canvas, x, y, w, h);
        }
    }


    public void qrImgRender(Graphics2D g2d, DetectSourceOptions.DetectSourceArea position, int x, int y, int w, int h) {
        g2d.setColor(sourceOptions.getColor(position));
        g2d.fillRect(x, y, w, h);
    }

    public void qrTxtRender(char[][] context, DetectSourceOptions.DetectSourceArea position, int x, int y, int w, int h) {
        char ch = '⬛';
        for (int i = x; i < w; i++) {
            for (int j = y; j < h; h++) {
                if (position == DetectSourceOptions.DetectSourceArea.BG) {
                    context[i][j] = ' ';
                } else {
                    context[i][j] = ch;
                }
            }
        }
    }

    public void qrSvgRender(List<SvgTag> svgList, DetectSourceOptions.DetectSourceArea position, int x, int y, int w, int h) {
        TextSvgTag svgTag = new TextSvgTag();
        if (position == DetectSourceOptions.DetectSourceArea.BG) {
            svgTag.setText("⬛");
        } else {
            svgTag.setText(" ");
        }
        svgTag.setX(x).setY(y).setW(w).setH(h).setColor(sourceOptions.getColor());
        svgList.add(svgTag);
    }

}
