package com.github.hui.quick.plugin.qrcode.v3.canvas;

import com.github.hui.quick.plugin.qrcode.v3.resources.RenderSource;
import com.github.hui.quick.plugin.qrcode.v3.templates.svg.SvgTag;

import java.util.List;

/**
 * svg 格式二维码画布
 *
 * @author yihui
 * @date 2022/6/10
 */
public class SvgQrCanvas implements QrCanvas {
    List<SvgTag> svgContext;

    public List<SvgTag> getSvgContext() {
        return svgContext;
    }

    @Override
    public void draw(RenderSource<?> resource, int x, int y, int w, int h) {
        resource.render(this, x, y, w, h);
    }

    @Override
    public String output() {
        return null;
    }
}
