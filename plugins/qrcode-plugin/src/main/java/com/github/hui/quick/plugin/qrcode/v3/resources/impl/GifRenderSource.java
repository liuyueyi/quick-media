package com.github.hui.quick.plugin.qrcode.v3.resources.impl;

import com.github.hui.quick.plugin.base.gif.GifDecoder;
import com.github.hui.quick.plugin.qrcode.v3.canvas.QrCanvas;
import com.github.hui.quick.plugin.qrcode.v3.options.source.SourceOptions;

/**
 * @author
 * @date 2022/6/10
 */
public class GifRenderSource extends BasicRenderSource<GifDecoder> {
    private GifDecoder gif;

    public GifRenderSource(SourceOptions sourceOptions) {
        super(sourceOptions);
    }

    @Override
    public <K> void render(QrCanvas canvas, int x, int y, int w, int h) {

    }

    @Override
    public GifDecoder getSource() {
        return gif;
    }
}
