package com.github.hui.quick.plugin.qrcode.v3.resources;

import com.github.hui.quick.plugin.base.gif.GifDecoder;
import com.github.hui.quick.plugin.qrcode.v3.canvas.QrCanvas;

/**
 * @author
 * @date 2022/6/10
 */
public class GifRenderSource implements RenderSource<GifDecoder> {
    private GifDecoder gif;

    @Override
    public <K> void render(QrCanvas canvas, int x, int y, int w, int h) {

    }

    @Override
    public GifDecoder getSource() {
        return gif;
    }
}
