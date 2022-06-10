package com.github.hui.quick.plugin.qrcode.v3.canvas;

import com.github.hui.quick.plugin.qrcode.v3.resources.RenderSource;

/**
 * 文字二维码 画布
 *
 * @author
 * @date 2022/6/10
 */
public class StrQrCanvas implements QrCanvas {

    private char[][] texts;

    public char[][] getTexts() {
        return texts;
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
